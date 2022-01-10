package casing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {

        class Incrementer implements Callable<AtomicInteger> {

            @Override
            public AtomicInteger call() throws Exception {
                for (int i = 0; i < 1000; i++) {
                    counter.incrementAndGet();
                }
                return counter;
            }
        }

        class Decrementer implements Callable<AtomicInteger> {

            @Override
            public AtomicInteger call() throws Exception {
                for (int i = 0; i < 1000; i++) {
                    counter.decrementAndGet();
                }
                return counter;
            }
        }
        List<Future> futureList = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        try {
            for (int i = 0; i < 4; i++) {
                futureList.add(executorService.submit(new Incrementer()));
            }

            for (int i = 0; i < 4; i++) {
                futureList.add(executorService.submit(new Decrementer()));
            }
            futureList.forEach(future -> {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println(e.getMessage());
                }
            });
            System.out.println("counter = " + counter);
        } finally {
            executorService.shutdown();
        }
    }
}
