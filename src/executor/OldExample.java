package executor;

public class OldExample {
    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.println("I am running in " + Thread.currentThread().getName());
        };
        for (int i = 0; i < 10; i++) {
            new Thread(task).start();
        }
    }

}
