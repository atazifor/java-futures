package taz.amin;

import java.util.concurrent.*;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

/**
 * Exercise 4: Real-time Data Processing Pipeline
 * Objective: Create a simple data processing pipeline that mimics real-time data ingestion and transformation.
 * Imagine you’re receiving sensor data (e.g., temperature) every second. You need to:
 *
 * Collect the data asynchronously.
 * Process the data (e.g., converting it to another unit, filtering out invalid data).
 * Store or log the processed data.
 * Use CompletableFuture to build this pipeline, chaining tasks that process and log the data. Make sure to handle exceptions for invalid sensor data.
 *
 * Key Focus:
 *
 * Chain multiple CompletableFuture stages with transformations.
 * Use handle() or exceptionally() to gracefully handle errors.
 * Explore custom executors to control the processing threads.
 */
public class RealTimeDataProcessing {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // Simulate continuous data generation using ScheduledExecutorService
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        CompletableFuture<Void> producerFuture = CompletableFuture.runAsync(() -> {
            scheduler.scheduleAtFixedRate(() -> {
                Stream<Integer> temperatureStream = temperatureGenerator();
                CompletableFuture<Stream<Integer>> processingFuture =
                        CompletableFuture.supplyAsync(() -> processTemperature(temperatureStream), executor);
                // Consume processed data asynchronously
                processingFuture.thenAcceptAsync(RealTimeDataProcessing::consumeTemperature, executor);
            }, 0, 10, TimeUnit.SECONDS);
        }, executor);
    }
    // Simulate a temperature generator (continuous stream of temperatures)
    private static Stream<Integer> temperatureGenerator() {
        return Stream.generate(() -> RandomGenerator.getDefault().nextInt(1, 100))
                .limit(5) // Simulate generating 5 temperatures at a time
                .map(i -> {
                    try {
                        System.out.println("Generating Temperature");
                        Thread.sleep(1000*RandomGenerator.getDefault().nextInt(1,3));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Generated temperature: " + i);
                    return i;
                });
    }

    // Simulate processing temperatures (filter valid temperatures)
    private static Stream<Integer> processTemperature(Stream<Integer> temp) {
        return temp.filter(i -> i < 50) // Only allow temperatures less than 50
                .peek(i -> System.out.println("Processed valid temperature: " + i));
    }

    // Simulate consuming (e.g., storing or printing) processed temperatures
    private static void consumeTemperature(Stream<Integer> tempStream) {
        tempStream.forEach(i -> System.out.println("Consumed temperature: " + i));
    }

}
