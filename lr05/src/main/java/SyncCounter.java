import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class SyncCounter {
    private final AtomicLong count = new AtomicLong(0);

    public synchronized void increment(){
        count.incrementAndGet();
    }

    public long threadRace(int t, int iterations) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < t; i++){
            Thread thread = new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for (int j = 0; j < iterations; j++) {
                    increment();
                }
            });
            thread.start();
            threads.add(thread);
        }
        countDownLatch.countDown();
        for (Thread thread : threads) {
            thread.join();
        }

        return count.get();
    }
}
