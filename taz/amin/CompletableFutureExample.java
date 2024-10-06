package taz.amin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletableFuture<String> weatherFuture = CompletableFuture.supplyAsync(() -> fetchWeatherData(), executor);
        CompletableFuture<String> stockFuture = CompletableFuture.supplyAsync(() -> fetchStockData(), executor);

        weatherFuture.thenAccept(s -> System.out.println("weather = " + s));
        stockFuture.thenAccept(s -> System.out.println("stock = " + s));

        System.out.println("Fetching data asynchronously...");

        CompletableFuture.allOf(weatherFuture, stockFuture).join();

        executor.shutdown();

    }

    private static String fetchStockData()  {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "S&P 500: 4500";
    }

    private static String fetchWeatherData() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Sunny, 25°C";
    }
}
