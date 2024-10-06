package taz.amin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Exercise 5: Combine Results from Multiple Asynchronous Tasks
 * Objective: Simulate a system that performs multiple independent calculations
 * (e.g., calculating different mathematical functions or processing independent datasets).
 * Each task should run in parallel, and you should combine the results once all tasks have finished.
 * Use CompletableFuture to achieve this.
 *
 * Key Focus:
 *
 * Use CompletableFuture.allOf() to combine the results of independent tasks.
 * Collect the results into a list or aggregate them in some meaningful way.
 * Experiment with thenCombine() and thenCombineAsync() to combine results of two CompletableFutures.
 */
public class CombineAsyncTasks {
    public static void main(String[] args) {
        CompletableFuture<Double> areaFuture = CompletableFuture.supplyAsync(CombineAsyncTasks::computeArea);
        CompletableFuture<Double> circumferenceFuture = CompletableFuture.supplyAsync(CombineAsyncTasks::computeCircumference);
        CompletableFuture<Double> otherFuture = CompletableFuture.supplyAsync(CombineAsyncTasks::otherComputer);

        // Wait for area and circumference futures to complete using allOf()
        CompletableFuture<Void> allOf = CompletableFuture.allOf(areaFuture, circumferenceFuture, otherFuture);

        List<Double> result = allOf.thenApply(v -> {
            return Stream.of(areaFuture.join(), circumferenceFuture.join(), otherFuture.join())
                    .collect(Collectors.toList());
        }).join();

        System.out.println("result = " + result);

        Double res = areaFuture.thenCombineAsync(circumferenceFuture, (x, y) -> {
            double sum = x + y;
            System.out.println("Result of area + circumference " + sum);
            return sum;
        }).thenCombineAsync(otherFuture, (x, y) -> {
            double sum = x + y;
            System.out.println("Result of with other value " + sum);
            return sum;
        }).join();

        System.out.println("Final sum = " + res);

    }

    private static double computeArea() {
        try {
            System.out.println("Computing area ...");
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double area = RandomGenerator.getDefault().nextDouble(1, 100);
        System.out.println("Area = " + area);
        return area;
    }

    private static double computeCircumference() {
        try {
            System.out.println("Computing Circumference ...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double c = RandomGenerator.getDefault().nextDouble(5, 100);
        System.out.println("Circumference = " + c);
        return c;
    }

    private static double otherComputer() {
        try {
            System.out.println("Computing  ...");
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double c = RandomGenerator.getDefault().nextDouble(5, 200);
        System.out.println("Other = " + c);
        return c;
    }
}
