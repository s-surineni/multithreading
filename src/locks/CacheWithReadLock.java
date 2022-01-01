package locks;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheWithReadLock {
    private Map<Long, String> cache = new HashMap<>();
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    public String put(Long key, String value) {
        writeLock.lock();
        try {
            return cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public String get(Long key) {
        readLock.lock();
        try {
            return cache.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public static void main(String[] args) {
        CacheWithReadLock cache = new CacheWithReadLock();
        class Producer implements Callable<String> {
            private Random rand = new Random();

            public String call() throws Exception {
                int count = 1000;
                while (count >= 0) {
                    long key = rand.nextInt(1_000);
                    cache.put(key, Long.toString(key));
                    if (cache.get(key) == null) {
                        System.out.println("Key " + key + " has not been put in the map");
                    }
                    count--;
                }
                return "Done";
            }
        }
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        System.out.println("Adding value...");
        try {
            for (int i = 0; i < 4; i++) {
                executorService.submit(new Producer());
            }
        } finally {
            executorService.shutdown();
        }
    }
}
