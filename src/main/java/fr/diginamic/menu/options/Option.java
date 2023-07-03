package fr.diginamic.menu.options;

import java.util.Scanner;

/**
 * Option du menu
 * */
public abstract class Option {
    /**Chaine qui va apparaitre à l'écran pour présenter l'option*/
    private String displayedCaption;

    public Option(String displayedCaption) {
        this.displayedCaption = displayedCaption;
    }

    /**Traitement exécuté par l'option*/
    public abstract boolean executeOption();

    public String getDisplayedCaption() {
        return displayedCaption;
    }
}
