package taz.amin;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureThenAccept {
    public static void main(String[] args) {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "TASK -0 with thread: " + Thread.currentThread().getName();
        });

        cf.thenAccept(s -> {
            System.out.println("s = " + s);
            System.out.println("Accept with:  " + Thread.currentThread().getName());
        });

        cf.thenAcceptAsync(s -> {
            System.out.println("s = " + s);
            System.out.println("Accept Async with:  " + Thread.currentThread().getName());
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
