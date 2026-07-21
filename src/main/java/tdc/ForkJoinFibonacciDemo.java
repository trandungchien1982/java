package tdc;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinFibonacciDemo {

    static class FibonacciTask extends RecursiveTask<Integer> {

        private final int n;

        FibonacciTask(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n <= 1) {
                return n;
            }

            FibonacciTask f1 = new FibonacciTask(n - 1);
            FibonacciTask f2 = new FibonacciTask(n - 2);

            // Execute first branch asynchronously
            f1.fork();

            // Compute second branch in current worker
            int result2 = f2.compute();

            // Wait for first branch
            int result1 = f1.join();

            return result1 + result2;
        }
    }

    public void run() {

        int n = 40;

        ForkJoinPool pool = new ForkJoinPool();

        long start = System.currentTimeMillis();

        int result = pool.invoke(new FibonacciTask(n));

        long end = System.currentTimeMillis();

        System.out.println("Fibonacci(" + n + ") = " + result);
        System.out.println("Execution Time: " + (end - start) + " ms");

        pool.shutdown();
    }
}
