package taz.amin;

import java.util.concurrent.CompletableFuture;
import java.util.random.RandomGenerator;

/**
 * Exercise 3: Chain Dependent Tasks
 * Objective: Implement a sequence of tasks using CompletableFuture.
 * Each task depends on the result of the previous one. For example:
 *
 * The first task generates a random number.
 * The second task takes that number and doubles it.
 * The third task takes the doubled value and adds 10 to it.
 * Key Focus:
 *
 * Chain tasks using thenApply() and thenApplyAsync().
 * Experiment with mixing thenApply() and thenApplyAsync() to observe the effect of synchronous vs asynchronous task execution.
 */
public class CompletableFutureChaining {
    public static void main(String[] args) throws InterruptedException{
        CompletableFuture<Integer> randomIntFuture = CompletableFuture.supplyAsync(() -> generateRandomNumber());
        CompletableFuture<Integer> result = randomIntFuture
                .thenApplyAsync(CompletableFutureChaining::doubleNumber)
                .thenApply(CompletableFutureChaining::augmentNumber);
        Integer num = result.join();
        System.out.println("num = " + num);

    }

    private static Integer generateRandomNumber() {
        try {
            System.out.println("Generating random number ...");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int i = RandomGenerator.getDefault().nextInt(0, 10);
        System.out.println("Generated:  " + i + " Thread: " + Thread.currentThread().getName());
        return i;
    }

    private static int doubleNumber(int num) {
        try {
            System.out.println("Doubling " + num + " ...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int i = 2 * num;
        System.out.println(num + " doubled to  " + i + " Thread: " + Thread.currentThread().getName());
        return i;
    }

    private static int augmentNumber(int num) {
        try {
            System.out.println("Augmenting " + num + " ...");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int i = 10 + num;
        System.out.println(num + " augmented with 10 to  " + i + " Thread: " + Thread.currentThread().getName());
        return i;
    }
}
