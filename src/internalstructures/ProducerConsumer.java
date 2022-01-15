package internalstructures;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ProducerConsumer {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(50);
        class Consumer implements Callable<String> {

            @Override
            public String call() throws Exception {
                int i = 0;
                for (; i < 50; i++) {
                    queue.take();
                }
                return "Counsumed " + (i - 1);
            }
        }

        class Producer implements Callable<String> {

            @Override
            public String call() throws Exception {
                int j = 0;
                for (; j < 50; j++) {
                    queue.put(Integer.toString(j));
                }
                return "Produced " + (j - 1);
            }
        }

        List<Callable<String>> callables = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            callables.add(new Producer());
        }

        for (int i = 0; i < 2; i++) {
            callables.add(new Consumer());
        }
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try {
            List<Future<String>> futures = executorService.invokeAll(callables);
            System.out.println("Producer and Consumers launched");
            futures.forEach(future -> {
                try {
                    System.out.println(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Exception " + e.getMessage());
                }
            });
        } finally {
            executorService.shutdown();
        }
    }
}
