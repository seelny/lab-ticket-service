import org.model.Priority;
import org.model.Role;
import org.model.Ticket;
import org.repository.InMemoryTicketRepository;
import org.repository.TicketRepository;
import org.service.*;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        BrokenCounter br = new BrokenCounter();
        SyncCounter sc = new SyncCounter();
        QueueProcessing qp = new QueueProcessing();
        WorkerPool wp = new WorkerPool();
        SafeCache safeCache = new SafeCache();
        ParallelAgregation pa = new ParallelAgregation();

        System.out.println(br.threadRace(8, 100000));
        System.out.println(sc.threadRace(8, 100000));

        System.out.println("-------");
        qp.start();

        System.out.println("-------");
        wp.workerPool();

        System.out.println("-------");
        safeCache.startThreads();

        System.out.println("-------");
        pa.generateFile();
        Map<String, Long> seqMap = pa.oneStreamCount();
        Map<String, Long> parMap = pa.parallelStreamCount();

        long seqSum = seqMap.values().stream().mapToLong(Long::longValue).sum();
        long parSum = parMap.values().stream().mapToLong(Long::longValue).sum();

        System.out.println("Сумма: " + seqSum);
        System.out.println("Сумма:   " + parSum);
        System.out.println("Словари совпадают: " + seqMap.equals(parMap));

    }
}