public class Main {
    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            System.out.println("Thread is: " + Thread.currentThread().getName());
            System.out.println("Thread priority: " + Thread.currentThread().getPriority());
            System.out.println("Thread state: " + Thread.currentThread().getState());
            System.out.println("Deamon?: " + Thread.currentThread().isDaemon());
            System.out.println("---------------");
        };

        Thread t1 = new Thread(task);
        t1.start();
        t1.join();

        Thread t2 = Thread.ofPlatform().daemon().unstarted(task);
        t2.start();
        t2.join();

        Thread t3 = Thread.ofVirtual().unstarted(task);
        t3.start();
        t3.join();
        
    }
}
