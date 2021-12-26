package executor;

import java.util.concurrent.*;

public class ExecutorExample2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        Callable<String> task = () -> {
            Thread.sleep(300);
            return ("I am running in " + Thread.currentThread().getName());
        };
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try {
            for (int i = 0; i < 10; i++) {
//            new Thread(task).start();
                Future<String> future = executorService.submit(task);
//            System.out.println("I get: " + future.get());
                System.out.println("I get: " + future.get(100, TimeUnit.MILLISECONDS));


            }
        } finally {
            executorService.shutdown();
        }
    }

}
