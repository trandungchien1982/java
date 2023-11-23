package exception_in_lambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MainApp {

	static Logger log = LoggerFactory.getLogger(MainApp.class);

	public static void main(String[] args) throws Exception {
		log.info("Thực hiện UseCase có Exception được raise trong Lambda và check kết quả!");
		log.info(" --- Current thread: " + Thread.currentThread().getId());
		MainApp app = new MainApp();

		try {
			app.singleThread();
		} catch (Exception ex) {
			log.error(" >> Exception occur in SINGLE Thread ", ex);
		}

		log.info("\n\n\n");
		try {
			app.multiThread();
		} catch (Exception ex) {
			log.error(" >> Exception occur in MULTIPLE Thread ", ex);
		}

		log.info("\n\n\n");
		log.info("The final script in MainApp ... ");
		Thread.sleep(2000);
		log.info("The final script in MainApp 01 ... ");

	}

	void singleThread() {
		log.info("Process in Single Thread ...");

		List<String> listData = List.of("Value 01", "Value 02", "Value 03", "Value 04", "Value 05");
		long countVal = listData.stream()
			.filter(v -> {
				log.info("-------------------------------------------------");
				log.info("Call filters with value: " + v + " --- Current thread: " + Thread.currentThread().getId());
				return v.contains("Value");
			})
			.map(v -> {
				log.info("Call map() with add Suffix01, value: " + v + " --- Current thread: " + Thread.currentThread().getId());

				// Throw exception at Value 03
				if (v.equals("Value 03")) {
					throw new NullPointerException("Custom Exception with [Value 03]");
				}
				return v + " - Suffix01";
			})
			.map(data -> {
				log.info("Call map() with add MIDDLE, value: " + data + " --- Current thread: " + Thread.currentThread().getId());
				return data + " - Middle";
			})
			.map(v -> {
				log.info("Call map() with Suffix02, value: " + v + " --- Current thread: " + Thread.currentThread().getId());
				return v + " - Suffix02";
			}).count();

		log.info("The countVal in SINGLE thread = " + countVal);
	}

	void multiThread() {
		log.info("Process in Multiple Thread ...");
		List<String> listData = List.of("Item 01", "Item 02", "Item 03", "Item 04", "Item 05", "Item 06", "Item 07");
		long countVal = listData.parallelStream()
			.filter(v -> {
				log.info("-------------------------------------------------");
				log.info("Process value: " + v + " [Filters] --- Current thread: " + Thread.currentThread().getId());
				return v.contains("Item");
			})
			.map(v -> {
				log.info("Process value: " + v + " [Mapping Suffix01] --- Current thread: " + Thread.currentThread().getId());

				// Throw exception at Item 03
				if (v.equals("Item 03")) {
					throw new NullPointerException("Another Exception with [Item 03]");
				}
				return v;
			})
			.map(v -> {
				log.info("Process value: " + v + " [Mapping Middle] --- Current thread: " + Thread.currentThread().getId());
				return v;
			})
			.map(v -> {
				log.info("Process value: " + v + " [Mapping Suffix02] --- Current thread: " + Thread.currentThread().getId());
				return v;
			}).count();

		log.info("The countVal in MULTIPLE threads = " + countVal);
	}
}
