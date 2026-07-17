package tdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CompletableFutureDemo {

    private static final Logger log = LoggerFactory.getLogger(CompletableFutureDemo.class);

    public Map<String, List<String>> execute(List<String> items, int poolSize) {
        ConcurrentHashMap<String, List<String>> resultMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Throwable> errorMap = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        AtomicBoolean cancelled = new AtomicBoolean(false);

        try {
            List<CompletableFuture<Void>> futures = new ArrayList<>(items.size());

            for (String item : items) {
                CompletableFuture<Void> future = CompletableFuture
                        .supplyAsync(() -> JobProcessor.processItem(item, cancelled), executor)
                        .thenAccept(units -> {
                            resultMap.put(item, units);
                            log.info("CompletableFuture completed -- : {}", item);
                        })
                        .exceptionally(exception -> {
                            Throwable cause = unwrap(exception);
                            errorMap.put(item, cause);
                            log.error("CompletableFuture failed for {}", item, cause);
                            return null;
                        });

                futures.add(future);
            }

            log.info(" --- Before allOf() : " + new Date());
            CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
            log.info(" --- After allOf() : " + new Date());

            if (!errorMap.isEmpty()) {
                log.warn("Completed with {} failed job(s)", errorMap.size());
            }

            return Map.copyOf(resultMap);
        } finally {
            shutdown(executor);
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
