package fr.diginamic.menu.options;

/**
 * Option du menu
 * */
public abstract class Option {
    /**Chaine qui va apparaitre à l'écran pour présenter l'option*/
    protected String displayedCaption;

    /**Traitement exécuté par l'option*/
    abstract void executeOption();

    public String getDisplayedCaption() {
        return displayedCaption;
    }
}
