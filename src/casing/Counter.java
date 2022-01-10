package casing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Counter {
    private static int counter = 0;


    public static void main(String[] args) {

        class Incrementer implements Callable<Integer> {

            @Override
            public Integer call() throws Exception {
                for (int i = 0; i < 1000; i++) {
                    counter++;
                }
                return counter;
            }
        }

        class Decrementer implements Callable<Integer> {

            @Override
            public Integer call() throws Exception {
                for (int i = 0; i < 1000; i++) {
                    counter--;
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
