package tdc;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinSumArrayDemo {

    static class SumTask extends RecursiveTask<Long> {

        private static final int THRESHOLD = 1_000_000 * 100;

        private final long[] array;
        private final int start;
        private final int end;

        SumTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {

            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            }

            int mid = (start + end) / 2;

            SumTask left = new SumTask(array, start, mid);
            SumTask right = new SumTask(array, mid, end);

            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();

            return leftResult + rightResult;
        }
    }

    public void run() {

        final int SIZE = 100_000_000;

        long[] numbers = new long[SIZE];
        Random random = new Random(12345);

      for (int i = 0; i < SIZE; i++) {
        numbers[i] = random.nextInt(10);}

        // Sequential
        long t1 = System.currentTimeMillis();

        long sequentialSum = 0;
        for (long n : numbers) {
            sequentialSum += n;
        }

        long t2 = System.currentTimeMillis();

        // ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();

        long t3 = System.currentTimeMillis();

        long parallelSum = pool.invoke(
                new SumTask(numbers, 0, numbers.length));

        long t4 = System.currentTimeMillis();

        pool.shutdown();

        System.out.println("Sequential Sum : " + sequentialSum);
        System.out.println("ForkJoin Sum   : " + parallelSum);

        System.out.println("Sequential Time : " + (t2 - t1) + " ms");
        System.out.println("ForkJoin Time   : " + (t4 - t3) + " ms");
    }
}
