package module1;

public class RunCircularDep {
    public static void main(String[] args) throws InterruptedException {
        CircularDepA cDep = new CircularDepA();
        Runnable r1 = () -> cDep.a();
        Runnable r2 = () -> cDep.b();

        Thread t1 = new Thread(r1);
        t1.start();

        Thread t2 = new Thread(r2);
        t2.start();

        t1.join();
        t2.join();
    }
}
