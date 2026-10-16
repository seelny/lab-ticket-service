import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelAgregation {

    private final String path = "status_lines.txt";
    private static final String[] TOKENS = {"NEW", "IN_PROGRESS", "DONE", "LOW", "MEDIUM", "HIGH"};
    private static final int TOTAL_LINES = 10_000;

    public void generateFile() throws IOException {
        Random random = new Random();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < TOTAL_LINES; i++) {
                int id = random.nextInt(TOKENS.length);
                bufferedWriter.write(TOKENS[id]);
                bufferedWriter.newLine();
            }
        }
    }

    public Map<String, Long> oneStreamCount() {
        long startTime = System.currentTimeMillis();

        Map<String, Long> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                map.merge(line, 1L, Long::sum);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("время выполнения 1 потока: " + elapsedTime + " мс");
        return map;
    }

    public Map<String, Long> parallelStreamCount() {
        long startTime = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool(4);

        try {
            Map<String, Long> result = pool.submit(() -> {
                try (Stream<String> lines = Files.lines(Path.of(path))) {
                    return lines.parallel()
                            .collect(Collectors.groupingBy(
                                    Function.identity(),
                                    Collectors.counting()
                            ));
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }).get();

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Время выполнения 4 потоков:" + elapsedTime + " мс");
            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            pool.shutdown();
        }
    }
}