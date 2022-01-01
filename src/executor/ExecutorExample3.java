package executor;

import java.util.concurrent.*;

public class ExecutorExample3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<String> callable = () -> {
            throw new IllegalStateException("Thrown from " + Thread.currentThread().getName());
        };
        try {
            Future<String> future = executorService.submit(callable);
            future.get();
        } finally {
            executorService.shutdown();
        }
    }
}
