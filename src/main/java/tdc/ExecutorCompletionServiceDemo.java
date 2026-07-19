package tdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ExecutorCompletionServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(ExecutorCompletionServiceDemo.class);

    public Map<String, List<String>> execute(List<String> items, int poolSize) {
        ConcurrentHashMap<String, List<String>> resultMap = new ConcurrentHashMap<>();
        // Using Virtual Thread instead of real thread
        // ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        CompletionService<JobResult> completionService = new ExecutorCompletionService<>(executor);
        AtomicBoolean cancelled = new AtomicBoolean(false);

        try {
            // First, submit all items and get all futures
            List<Future<JobResult>> futures = new LinkedList<>();
            for (String item : items) {
              futures.add(completionService.submit(() ->
                        new JobResult(item, JobProcessor.processItem(item, cancelled))
                ));
            }

            // Create Future for processing task in executors.
            // Try to get the first finished task
            for (int completed = 1; completed <= items.size(); completed++) {
                Future<JobResult> future = completionService.take();
                if (cancelled.get()) {
                  log.warn(" -- Cancelled at index: {} due to cancelled flag ...", completed);

                  break;
                }

                try {
                    JobResult result = future.get();
                    resultMap.put(result.key(), result.units());
                    log.info("Executor completed {}/{}: {}", completed, items.size(), result.key());
                } catch (ExecutionException exception) {
                    log.error("Executor job failed", exception.getCause());
                    log.warn(" --- We have an exception, set the cancelled to be true");
                    //cancelled.set(true);
                    // Cancel all other future
                    // Hủy những future chưa hoàn thành
                    futures.forEach(otherFuture -> {
                      if (!otherFuture.isDone()) {
                        otherFuture.cancel(true);
                      }
                    });
                    break;
                }
            }

            return Map.copyOf(resultMap);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Main executor flow was interrupted", exception);
        } finally {
            shutdown(executor);
        }
    }

  private void checkCancellation(
          AtomicBoolean cancelled
  ) {
    if (cancelled.get()
            || Thread.currentThread().isInterrupted()) {
      throw new JobCancelledException(
              "Workflow was cancelled"
      );
    }
  }

  private static class JobCancelledException
          extends RuntimeException {

    public JobCancelledException(String message) {
      super(message);
    }

    public JobCancelledException(
            String message,
            Throwable cause
    ) {
      super(message, cause);
    }
  }

  private Throwable unwrap(Throwable exception) {
    if (exception instanceof CompletionException && exception.getCause() != null) {
      return exception.getCause();
    }
    return exception;
  }

    private void shutdown(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException exception) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
