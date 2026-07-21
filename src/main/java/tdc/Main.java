package tdc;

  import org.slf4j.Logger;
  import org.slf4j.LoggerFactory;

  import java.util.Arrays;

public class Main {
  private static Logger logger = LoggerFactory.getLogger(Main.class);
  public static void main(String[] args) {
    logger.info(" -- Demo for ForkJoinPool ... ");
    logger.info(" -- Parameters: " + Arrays.asList(args));
    String action = Arrays.stream(args).findFirst().orElse("");

    new Main().run(action);
  }

  public void run(String action) {
    logger.info("");
    logger.info(" -------------------------------------------------------------------- ");
    logger.info(" -------------------------------------------------------------------- ");
    long startTime = System.currentTimeMillis();

    logger.info("ForkJoinPool - FibonacciDemo() ... ");
    new ForkJoinFibonacciDemo().run();

    long endTime = System.currentTimeMillis();
    logger.info(" --- Total time for choice: " + action + " " + ( (endTime - startTime) / 1000) + " seconds");
  }
}