import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * ConcurrentHashMap
 */
public class CHashMap {

    public static void main(String[] args) {
        var set = ConcurrentHashMap.<String>newKeySet();

        Runnable task = () ->  set.add(Thread.currentThread().toString());

        int N_TASKS = 1000;
        try(var executor = Executors.newFixedThreadPool(10)) {
            for(int i = 0; i < N_TASKS; i++) {
                executor.submit(task);
            }
        }
        System.out.printf("Threads used %s%n", set.size());
        System.out.println("---------------");
        System.out.printf("%s%n", set);
    }
}