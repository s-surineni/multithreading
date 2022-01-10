package casing;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MyAtomicCounter extends AtomicInteger {
    private static Unsafe unsafe = null;

    static {
        Field unsafeField;
        try {
            unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private AtomicInteger countIncrement = new AtomicInteger(0);

    public MyAtomicCounter(int counter) {
        super(counter);
    }

    public int myIncrementAndGet() {
        long valueOffset = 0L;
        try {
            valueOffset = unsafe.objectFieldOffset(AtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        int v;
        do {
            v = unsafe.getIntVolatile(this, valueOffset);
            countIncrement.incrementAndGet();
        } while (!unsafe.compareAndSwapInt(this, valueOffset, v, v+1));
        return v;
    }
    public int getIncrements() {
        return this.countIncrement.get();
    }

    private static MyAtomicCounter counter = new MyAtomicCounter(0);

    public static void main(String[] args) {

        class Incrementer implements Callable<MyAtomicCounter> {

            @Override
            public MyAtomicCounter call() throws Exception {
                for (int i = 0; i < 1000; i++) {
                    counter.myIncrementAndGet() ;
                }
                return counter;
            }
        }

        class Decrementer implements Callable<MyAtomicCounter> {

            @Override
            public MyAtomicCounter call() throws Exception {
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
            System.out.println("# increments = " + counter.getIncrements());
        } finally {
            executorService.shutdown();
        }
    }
}
