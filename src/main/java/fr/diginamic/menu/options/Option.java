package fr.diginamic.menu.options;

public abstract class Option {
    protected String displayedCaption;

    abstract void executeOption();

    public String getDisplayedCaption() {
        return displayedCaption;
    }
}
