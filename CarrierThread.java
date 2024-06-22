import java.util.concurrent.Executors;

public class CarrierThread {

    /*
     * Carrier Threads: is the Platform thread taken from specific ForkJoinPool dedicated to virtual threads.
     * Virtual Threads: is executed on top of the Carrier Thread.
     * +-------------------+
     * |  Virtual Threads  |
     * +-------------------+
     * +-------------------+
     * |  Platform Thread  |
     * |-------------------+    
     * 
     * When a virtual thread is created, it is assigned to a specific ForkJoinPool and one of the available carrier threads is going to execute it.
     * 
     * The number of Carrier Threads is equal to the number of CPU cores.
     * in Fork/Join pool, there is a waiting list for each carrier thread.
     * the virtual threads added to those waiting lists to be executed.
     * */

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> System.out.println("CurrentThread is: " + Thread.currentThread());

        var thread = Thread.ofVirtual().unstarted(task);

        thread.start();
        thread.join();

        try(var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(task);
        }
    }
}
