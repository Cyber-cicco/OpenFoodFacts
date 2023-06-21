package fr.diginamic.threader;

public class VirtualThread {

    public static Thread getThread(String name, Runnable runnable){
        return Thread.ofVirtual()
                .name(name)
                .start(runnable);
    }
}
