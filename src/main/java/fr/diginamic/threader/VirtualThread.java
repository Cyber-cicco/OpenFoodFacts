package fr.diginamic.threader;

/**
 * Classe utilitaire permettant de simplfier la création
 * d'une virtual Thread
 * */
public class VirtualThread {

    public static Thread getThread(String name, Runnable runnable){
        return Thread.ofVirtual()
                .name(name)
                .start(runnable);
    }
}
