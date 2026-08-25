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

    System.out.println("\n===== Menu for parameters =====");
    System.out.println("01 - Fibonacci using ForkJoinPool ... ");
    System.out.println("02 - Sum Array using ForkJoinPool ... ");
    System.out.println("\nPlease select your choice as following : ./gradlew run --args=\"{choiceKey}\"\n");

    new Main().run(action);
  }

  public void run(String action) {
    logger.info("");
    logger.info(" -------------------------------------------------------------------- ");
    logger.info(" -------------------------------------------------------------------- ");
    long startTime = System.currentTimeMillis();

    switch (action) {
      case "01" -> {
        logger.info("ForkJoinPool - FibonacciDemo() ... ");
        new ForkJoinFibonacciDemo().run();
      }
      case "02" -> {
        logger.info("ForkJoinPool - Sum Array () ... ");
        new ForkJoinSumArrayDemo().run();
      }
    }

    long endTime = System.currentTimeMillis();
    logger.info(" --- Total time for choice: " + action + " " + ( (endTime - startTime) / 1000) + " seconds");
  }
}