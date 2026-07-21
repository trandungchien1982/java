package tdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class FutureDemo {

    private static final Logger log = LoggerFactory.getLogger(FutureDemo.class);

    public Map<String, List<String>> execute(List<String> items, int poolSize) {
        ConcurrentHashMap<String, List<String>> resultMap = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        CompletionService<JobResult> completionService = new ExecutorCompletionService<>(executor);
        AtomicBoolean cancelled = new AtomicBoolean(false);

        try {
            for (String item : items) {
                completionService.submit(() ->
                        new JobResult(item, JobProcessor.processItem(item, cancelled))
                );
            }

            for (int completed = 1; completed <= items.size(); completed++) {
                Future<JobResult> future = completionService.take();
                try {
                    JobResult result = future.get();
                    resultMap.put(result.key(), result.units());
                    log.info("Executor completed {}/{}: {}", completed, items.size(), result.key());
                } catch (ExecutionException exception) {
                    log.error("Executor job failed", exception.getCause());
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
