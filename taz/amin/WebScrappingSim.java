package taz.amin;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Exercise 2: Simulate Web Scraping with CompletableFuture
 * Objective: Simulate a system where multiple websites (use URLs as identifiers) are "scraped" asynchronously.
 * Each task represents a website scrape that returns a string result after a delay
 * (to simulate fetching data). Use CompletableFuture to start the scraping tasks in parallel and collect the results.
 *
 * Key Focus:
 *
 * Use CompletableFuture.supplyAsync() to simulate multiple asynchronous "scraping" operations.
 * Combine all results using CompletableFuture.allOf() and collect the results into a list.
 * Handle exceptions for any failed scrape operations with exceptionally().
 */
public class WebScrappingSim {
    public static void main(String[] args) {
        List<CompletableFuture<String>> completableFutureList = IntStream.rangeClosed(0, 4)
                .mapToObj(i -> "http://" + i + ".com")
                .map(s ->
                        CompletableFuture.supplyAsync(() -> scrapeWebsite(s))
                                .orTimeout(3, TimeUnit.SECONDS) //throw exception if long running tasks
                                .exceptionally(ex -> "Error occurred for uri: " + s) //don't crash in case task fails
                ).collect(Collectors.toList());

        System.out.println("Before all");
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]));
        System.out.println("After all ...");

        CompletableFuture<List<String>> allResults = allFutures.thenApply(v ->
                completableFutureList.stream()
                        .map(CompletableFuture::join)  // Get the results from each future
                        .collect(Collectors.toList())
        );

        try {
            // Get the final results and print them
            List<String> results = allResults.join();
            results.forEach(System.out::println);
        } catch (RuntimeException e) {
            System.out.println("An exception occurred: " + e.getMessage());
        }

    }

    private static String scrapeWebsite(String url) {
        try {
            System.out.println("scraping website = " + url);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("An interrupted exception occurred");
            throw new RuntimeException(e);
        }
        return "Content from " + url;
    }
}
