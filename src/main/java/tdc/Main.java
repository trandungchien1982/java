package tdc;

  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;

  import java.util.Arrays;
  import java.util.LinkedList;
  import java.util.List;
  import java.util.Optional;
  import java.util.stream.Stream;

public class Main {
  private static Logger logger = LoggerFactory.getLogger(Main.class);
  public static void main(String[] args) {
    logger.info(" -- Demo for Executors, Future, CompletableFuture ... ");
    logger.info(" -- Parameters: " + Arrays.asList(args));
    String action = Arrays.stream(args).findFirst().orElse("");

    System.out.println("\n===== Menu for parameters =====");
    System.out.println("01 - FutureDemo (using Future + submit tasks)");
    System.out.println("02 - CompletableFuture Demo with pipelines");
    System.out.println("03 - CompletableFuture Demo + Failfast : Stop with first exception");
    System.out.println("\nPlease select your choice as following : ./gradlew run --args=\"{choiceKey}\"\n");

    new Main().run(action);
  }

  public void run(String action) {

    List<String> list = getList();
    logger.info("");
    logger.info(" -------------------------------------------------------------------- ");
    logger.info(" -------------------------------------------------------------------- ");
    long startTime = System.currentTimeMillis();

    switch (action) {
      case "01":
        logger.info("FutureDemo() ... ");
        new FutureDemo().execute(list, 100);
        break;

      case "02":
        logger.info("CompletableFutureDemo() ... ");
        new CompletableFutureDemo().execute(list, 100);
        break;

      case "03":
        logger.info("CompletableFutureFailFastDemo() ... ");
        new CompletableFutureFailFastDemo().execute(list, 100);
        logger.info("Process action [2]");
        break;

      default:
        logger.warn("Invalid action input: {}", action);
    }

    long endTime = System.currentTimeMillis();
    logger.info(" --- Total time for choice: " + action + " " + ( (endTime - startTime) / 1000) + " seconds");
  }

  private List<String> getList() {
    List<String> result = new LinkedList<>();
    for (int i = 1; i <= 1000; i++) {
      result.add("Item_" + i);
    }
    return result;
  }
}