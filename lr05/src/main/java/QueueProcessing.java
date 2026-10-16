import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class QueueProcessing {
    BlockingQueue<String> queue = new ArrayBlockingQueue<>(8);
    public static final String POISON_PILL = "POISON_PILL";

    private final AtomicLong counter = new AtomicLong(0);

    private final AtomicLong ticketIdGenerator = new AtomicLong(0);

    private Runnable runConsumer() {
        return () -> {
            while (true){
                String element = null;
                try {
                    element = queue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (POISON_PILL.equals(element)){
                    break;
                }
                else{
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    counter.incrementAndGet();
                }
            }
        };
    }

    private Runnable createProducer(int itemsToProduce) {
        return () -> {
            for (int i = 0; i < itemsToProduce; i++) {
                String title = ("Ticket " + ticketIdGenerator.incrementAndGet());
                try {
                    queue.put(title);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        };
    }

    public void start() throws InterruptedException {
        int producersCount = 2;
        int consumersCount = 3;
        int itemsPerProducer = 25;

        List<Thread> producerThreads = new ArrayList<>();
        List<Thread> consumerThreads = new ArrayList<>();

        for (int i = 0; i < consumersCount; i++) {
            Thread consumer = new Thread(runConsumer(), "Consumer-" + (i + 1));
            consumerThreads.add(consumer);
            consumer.start();
        }

        for (int i = 0; i < producersCount; i++) {
            Thread producer = new Thread(createProducer(itemsPerProducer), "Producer-" + (i + 1));
            producerThreads.add(producer);
            producer.start();
        }


        for (Thread producer : producerThreads) {
            producer.join();
        }

        for (int i = 0; i < consumersCount; i++) {
            queue.put(POISON_PILL);
        }

        for (Thread consumer : consumerThreads) {
            consumer.join();
        }

        long produced = ticketIdGenerator.get();
        long processed = counter.get();

        System.out.println("Создано задач:     " + produced);
        System.out.println("Обработано задач:  " + processed);
    }

}
