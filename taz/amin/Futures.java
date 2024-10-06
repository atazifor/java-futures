package taz.amin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Futures {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Future<String> weatherFuture = executor.submit(() -> fetchWeatherData());
        Future<String> stockFuture = executor.submit(() -> fetchStockData());
        Future<String> newsFuture = executor.submit(() -> fetchNews());
        // We can now do other things while waiting for the data.
        System.out.println("Fetching data...");

        // Get results when ready (blocking, but at different times)
        String weatherData = weatherFuture.get(); // We only block here once the result is needed.
        String stockData = stockFuture.get();
        String newsData = newsFuture.get();

        System.out.println("Weather: " + weatherData);
        System.out.println("Stocks: " + stockData);
        System.out.println("News: " + newsData);

        executor.shutdown();
    }

    private static String fetchWeatherData() throws InterruptedException {
        // Simulate a 2-second delay
        Thread.sleep(2000);
        return "Sunny, 25°C";
    }

    private static String fetchStockData() throws InterruptedException {
        // Simulate a 3-second delay
        Thread.sleep(3000);
        return "S&P 500: 4500";
    }

    private static String fetchNews() throws InterruptedException {
        // Simulate a 1-second delay
        Thread.sleep(1000);
        return "Breaking News: Java Futures explained!";
    }
}
