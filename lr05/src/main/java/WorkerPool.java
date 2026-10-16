import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WorkerPool {

    private String computeSha256(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(encodedHash);
    }

    public void workerPool() {
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            String task = "ticket-" + i;
            futures.add(executor.submit(() -> computeSha256(task)));
        }

        int successCount = 0;
        int errorCount = 0;
        for (Future<String> future : futures) {
            try {
                String result = future.get();
                successCount++;
            } catch (ExecutionException e) {
                errorCount++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                errorCount++;
            }
        }
        executor.shutdown();

        System.out.println("Успешно: " + successCount + ", Сбоев: " + errorCount
                + ", Суммарное время: " + (System.currentTimeMillis() - startTime) + " мс");
    }
}