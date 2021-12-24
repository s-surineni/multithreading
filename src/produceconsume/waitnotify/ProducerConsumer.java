package produceconsume.waitnotify;

public class ProducerConsumer {
    private static Object lock = new Object();
    private static int[] buffer;
    private static int count;

    static class Poducer {
        void produce() throws InterruptedException {
            synchronized (lock) {
                while (isFull(buffer)) {
                    lock.wait();
                }
                buffer[count++] = 1;
                lock.notify();
            }
        }
    }

    static class Consumer {
        void consume() throws InterruptedException {
            synchronized (lock) {
                while (isEmpty(buffer)) {
                    lock.wait();
                }
                buffer[--count] = 0;
                lock.notify();
            }
        }
    }

    static boolean isEmpty(int[] buffer) {
        return count == 0;
    }

    static boolean isFull(int[] buffer) {
        return count == buffer.length;
    }

    public static void main(String[] args) throws InterruptedException {
        buffer = new int[10];
        count = 0;
        Poducer producer = new Poducer();
        Consumer consumer = new Consumer();
        Runnable produceTask = () -> {
            for (int i = 0; i < 50; i++) {
                try {
                    producer.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Done producing");
        };
        Runnable consumeTask = () -> {
//            for (int i = 0; i < 50; i++) {
            for (int i = 0; i < 45; i++) {
                try {
                    consumer.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Done consuming");

        };
        Thread consumerThread = new Thread(consumeTask);
        Thread producerThread = new Thread(produceTask);
        consumerThread.start();
        producerThread.start();
        consumerThread.join();
        producerThread.join();
        System.out.println("Data in the buffer: " + count);
    }
}
