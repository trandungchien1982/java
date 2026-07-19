package tdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class JobProcessor {

    private static Logger logger = LoggerFactory.getLogger(JobProcessor.class);

    public static final int DEFAULT_UNIT_COUNT = 15;
    public static final long DEFAULT_ITEM_DELAY_MS = 500;
    public static final long DEFAULT_UNIT_DELAY_MS = 10;

    private JobProcessor() {
    }

    public static List<String> processItem(String key, AtomicBoolean cancelled) {
        return processItem(key, DEFAULT_ITEM_DELAY_MS, DEFAULT_UNIT_DELAY_MS, DEFAULT_UNIT_COUNT, cancelled);
    }

    public static List<String> processItem(
            String key,
            long itemDelayMs,
            long unitDelayMs,
            int unitCount,
            AtomicBoolean cancelled
    ) {
        if (cancelled.get() || Thread.currentThread().isInterrupted()) {
          logger.info(" ---------------- >>>>> BREAK due to thread INTERRUPT ... ");
          return null;
        }

        //logger.info(" ---------- Current Thread 1: {}, isVirtual Thread: {}", Thread.currentThread().getId(), Thread.currentThread().isVirtual());

        // Randomly create exception:
        int idx = Integer.valueOf(key.replace("Item_", ""));
        if (idx % 100 == 0) {
            logger.warn("Exception at key : {}", key);
            throw new RuntimeException("Custom Exception at key: " + key);
        }

        sleep(itemDelayMs, "processing item " + key);

        sleep(5000, "dump sleep 10s ...");

        if (cancelled.get() || Thread.currentThread().isInterrupted()) {
          logger.info(" ---------------- >>>>> BREAK due to thread INTERRUPT 2 ... ");
          return null;
        }

        List<String> units = new ArrayList<>(unitCount);
        for (int index = 1; index <= unitCount; index++) {
            if (cancelled.get()) {
              logger.info(" ---------------- >>>>> BREAK due to thread INTERRUPT 3 ... ");
              return null;
            }
            sleep(unitDelayMs, "adding unit " + index + " for " + key);
            units.add("Unit value with " + key
                    + " at index " + index
                    + " at time " + Instant.now());
        }

        return List.copyOf(units);
    }

    public static List<String> createItems(int itemCount) {
        List<String> items = new ArrayList<>(itemCount);
        for (int index = 1; index <= itemCount; index++) {
            items.add("item-" + index);
        }
        return List.copyOf(items);
    }

    private static void sleep(long milliseconds, String operation) {
      //logger.info(" --- Sleep time in millis: {}", milliseconds);
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while " + operation, exception);
        }
    }
}
