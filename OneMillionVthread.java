import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class OneMillionVthread {

    public static final Pattern POOL_PATTERN = Pattern.compile("ForkJoinPool-\\d?");
    public static final Pattern WORKER_PATTERN = Pattern.compile("worker-\\d?");

    public static void main(String[] args) throws InterruptedException {
        Set<String> poolNames = ConcurrentHashMap.newKeySet();
        Set<String> pThreadNames = ConcurrentHashMap.newKeySet();

        int N_THREADS = 1_000_000;

        var threads = IntStream.range(0, N_THREADS)
            .mapToObj(i -> Thread.ofVirtual().unstarted(
                () -> {
                    String poolName = readPoolName();
                    poolNames.add(poolName);
                    String workerName = readWorkerName();
                    pThreadNames.add(workerName);
                })
        ).toList();

        Instant begin = Instant.now();

        for(var thread : threads) {
            thread.start();
            thread.join();
        }

        Instant end = Instant.now();

        System.out.printf("Virtual Threads: %s%n", N_THREADS);
        System.out.printf("Cores: %s%n", Runtime.getRuntime().availableProcessors());
        System.out.println("Time taken: " + Duration.between(begin, end).toMillis() + "ms");

        System.out.println("---------------");
        System.out.printf("Pool count: %s%n", poolNames.size());
        poolNames.forEach(System.out::println);

        System.out.println("---------------");
        System.out.printf("Platform Threads: %s%n", pThreadNames.size());
    }

    private static String readWorkerName() {
        return WORKER_PATTERN
            .matcher(Thread.currentThread().toString())
            .results()
            .findFirst()
            .get()
            .group();
    }

    private static String readPoolName() {
        return POOL_PATTERN
            .matcher(Thread.currentThread().toString())
            .results()
            .findFirst()
            .get()
            .group();
    }
    
}
