package fr.diginamic.test;

import fr.diginamic.threader.VirtualThread;

import static java.lang.Thread.sleep;

public class TestThreads {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = VirtualThread.getThread("caca", ()->{
            try {
                sleep(3000);
                System.out.println("Hello");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.join();
        System.out.println("World");
    }
}
