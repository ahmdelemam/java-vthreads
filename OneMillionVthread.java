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

        /*
        Separation of Concerns:
            Starting Threads: The first loop ensures that all threads are started as soon as possible. 
                              This is important because the threads may perform independent or interdependent tasks that can start concurrently without waiting for each other.
            Joining Threads: The second loop waits for all threads to complete. This ensures that the main program does not proceed until all threads have finished their tasks.
        Concurrency Efficiency:
            By starting all threads in the first loop, you maximize concurrency.
            All threads can begin executing their tasks concurrently rather than sequentially waiting for the previous thread to complete (which would happen if starting and joining were done in a single loop).
        Deadlock Avoidance:
            Starting and joining threads in the same loop could lead to deadlock scenarios, especially if the threads depend on each other in some way.
            By starting all threads first, you ensure that all threads are running before any of them are joined.
        */

        for(var thread : threads) {
            thread.start();
        }

        for(var thread : threads) {
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
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.printf("Used Memory: %d kB%n", usedMemory / 1024);
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
