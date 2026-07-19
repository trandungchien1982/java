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
    logger.info("Start new app with Virtual Threads ...");
    List<String> list = getList();

    logger.info("ExecutorCompletionServiceDemo() - Virtual Thread ... ");
    long startTime = System.currentTimeMillis();
    new ExecutorCompletionServiceDemo().execute(list, 10);
    long endTime = System.currentTimeMillis();
    logger.info(" --- Total time (Part I - Virtual Threads): " + ( (endTime - startTime) / 1000) + " seconds");
  }

  private List<String> getList() {
    List<String> result = new LinkedList<>();
    for (int i = 1; i <= 1000000; i++) {
      result.add("Item_" + i);
    }
    return result;
  }
}