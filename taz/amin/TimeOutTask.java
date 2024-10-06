package taz.amin;

import java.util.concurrent.*;

/**
 * Implement a Timeout Task Using Future
 * Objective: Write a program that submits a task using ExecutorService and Future.
 * The task should simulate a long-running operation (e.g., a 5-second sleep).
 * Add logic to attempt fetching the result with a timeout of 2 seconds.
 * If the task doesn’t complete within the timeout, cancel the task and handle the cancellation properly.
 * <p>
 * Key Focus:
 * <p>
 * Use ExecutorService to submit a task.
 * Use future.get(timeout, TimeUnit.SECONDS) for timeout handling.
 * Implement task cancellation with future.cancel().
 */
public class TimeOutTask {
    public static void main(String[] args) {
        // Create an ExecutorService with a thread pool of 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit a task that simulates a long-running operation
        Future<String> result = executor.submit(() -> {
            try {
                Thread.sleep(5000); // Simulate long-running task
            } catch (InterruptedException e) {
                System.out.println("Task was interrupted");
                return null;
            }
            return "Long-running task completed";
        });

        try {
            // Attempt to get the result with a timeout of 2 seconds
            String s = result.get(2, TimeUnit.SECONDS);
            System.out.println("Result: " + s);
        } catch (InterruptedException e) {
            System.out.println("Task was interrupted while waiting");
        } catch (ExecutionException e) {
            System.out.println("Exception occurred during task execution: " + e.getMessage());
        } catch (TimeoutException e) {
            // Timeout occurred; cancel the task
            result.cancel(true);  // true means it attempts to interrupt the running task
            System.out.println("Task was cancelled due to timeout");

            // Check if the task was successfully canceled
            if (result.isCancelled()) {
                System.out.println("Task successfully canceled");
            } else {
                System.out.println("Task cancellation failed");
            }
        }

        // Shutdown the executor service gracefully
        executor.shutdown();
        try {
            // Wait for all tasks to finish (if there are any)
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                System.out.println("Forcing shutdown...");
                executor.shutdownNow();  // Force shutdown if tasks are still running
            }
        } catch (InterruptedException e) {
            System.out.println("Shutdown interrupted");
            executor.shutdownNow();  // Force shutdown if interrupted
        }
    }
}
