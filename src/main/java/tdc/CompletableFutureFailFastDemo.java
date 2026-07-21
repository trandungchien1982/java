package tdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Multi Executors including breaking
 */
public final class CompletableFutureFailFastDemo {

    private static final Logger log = LoggerFactory.getLogger(CompletableFutureFailFastDemo.class);

    public Map<String, List<String>> execute(List<String> items, int poolSize) {
        ConcurrentHashMap<String, List<String>> resultMap = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        try {
            List<CompletableFuture<Void>> futures = new ArrayList<>(items.size());
            AtomicBoolean cancelled = new AtomicBoolean(false);

            for (String item : items) {
                CompletableFuture<Void> future = CompletableFuture
                        .supplyAsync(() -> JobProcessor.processItem(item, cancelled), executor)
                        .thenAccept(units -> {
                            resultMap.put(item, units);
                            log.info("CompletableFuture completed -- : {}", item);
                        });

                futures.add(future);
            }

            CompletableFuture<Void> failFast =
                    createFailFastFuture(futures, cancelled);

            failFast.join();

            return Map.copyOf(resultMap);
        } catch (CompletionException exception) {
          System.err.println(
                  "Workflow stopped because one job failed: "
                          + exception.getCause().getMessage()
          );
        } finally {
            shutdown(executor);
        }

        return null;
    }

  private CompletableFuture<Void> createFailFastFuture(
          List<? extends CompletableFuture<?>> futures,
          AtomicBoolean cancelled
  ) {
    CompletableFuture<Void> failureSignal =
            new CompletableFuture<>();

    for (CompletableFuture<?> future : futures) {
      future.whenComplete((result, exception) -> {
        if (exception != null
                && failureSignal.completeExceptionally(
                unwrap(exception)
        )) {

          cancelled.set(true);

          // Hủy những future chưa hoàn thành
          futures.forEach(otherFuture -> {
            if (!otherFuture.isDone()) {
              otherFuture.cancel(true);
            }
          });
        }
      });
    }

    CompletableFuture<Void> allCompleted =
            CompletableFuture.allOf(
                    futures.toArray(CompletableFuture[]::new)
            );

    // Future nào hoàn thành trước sẽ quyết định:
    // - failureSignal: một job lỗi
    // - allCompleted: tất cả job thành công
    return CompletableFuture.anyOf(
            failureSignal,
            allCompleted
    ).thenApply(ignored -> null);
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
