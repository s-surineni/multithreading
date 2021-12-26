package executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorExample {
    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.println("I am running in " + Thread.currentThread().getName());
        };
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
//            new Thread(task).start();
            executorService.execute(task);
        }
        executorService.shutdown();
    }

}
