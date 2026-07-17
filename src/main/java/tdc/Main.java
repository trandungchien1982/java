package tdc;

  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;

  import java.util.Arrays;
  import java.util.LinkedList;
  import java.util.List;

public class Main {
  private Logger logger = LoggerFactory.getLogger(getClass());
  public static void main(String[] A) {
    new Main().run();
  }

  public void run() {
    logger.info("Start new app ...");
    List<String> list = getList();

    logger.info("ExecutorCompletionServiceDemo() ... ");
    long startTime = System.currentTimeMillis();
    new ExecutorCompletionServiceDemo().execute(list, 20);
    long endTime = System.currentTimeMillis();
    logger.info(" --- Total time (Part I): " + ( (endTime - startTime) / 1000) + " seconds");

    logger.info("CompletableFutureDemo() ... ");
    logger.info(" -------------------------------------------------------------------- ");
    logger.info(" -------------------------------------------------------------------- ");
    startTime = System.currentTimeMillis();
    new CompletableFutureDemo().execute(list, 20);
    endTime = System.currentTimeMillis();
    logger.info(" --- Total time (Part II): " + ( (endTime - startTime) / 1000) + " seconds");


    logger.info("CompletableFutureBreakDemo() ... ");
    logger.info(" -------------------------------------------------------------------- ");
    logger.info(" -------------------------------------------------------------------- ");
    logger.info(" -------------------------------------------------------------------- ");
    startTime = System.currentTimeMillis();
    new CompletableFutureBreakDemo().execute(list, 20);
    endTime = System.currentTimeMillis();
    logger.info(" --- Total time (Part III): " + ( (endTime - startTime) / 1000) + " seconds");
  }

  private List<String> getList() {
    List<String> result = new LinkedList<>();
    for (int i = 1; i <= 1000; i++) {
      result.add("Item_" + i);
    }
    return result;
  }
}