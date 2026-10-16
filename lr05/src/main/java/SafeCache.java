import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class SafeCache {
    private final Map<String, String> map = new ConcurrentHashMap<>();
    private final AtomicInteger loadCount = new AtomicInteger(0);

    public String getOrLoad(String id, Function<String, String> loader) {
        return map.computeIfAbsent(id, loader);
    }

    public String loader(String id) {
        loadCount.incrementAndGet();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Значение для " + id;
    }

    public void startThreads() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch startGate = new CountDownLatch(1);
        List<Future<String>> futures = new ArrayList<>();

        String targetKey = "ticket-42";

        for (int i = 0; i < 10; i++) {
            futures.add(executor.submit(() -> {
                startGate.await();
                return getOrLoad(targetKey, this::loader);
            }));
        }

        startGate.countDown();

        for (Future<String> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println(e.getMessage());
            }
        }

        executor.shutdown();

        System.out.println("Вызовов loader: " + loadCount.get());
    }
}
