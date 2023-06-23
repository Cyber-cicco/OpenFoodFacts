package fr.diginamic.menu;


import fr.diginamic.menu.options.Option;

import java.util.List;

public class Menu {
    private List<Option> options;

    public Menu() {
    }

    public void showMenu(){
        options.forEach(option -> System.out.println(option.getDisplayedCaption()));
    }
}
