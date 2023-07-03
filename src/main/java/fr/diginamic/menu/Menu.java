package fr.diginamic.menu;


import fr.diginamic.menu.options.*;
import fr.diginamic.utils.ScannerManager;

import java.util.*;

public class Menu {
    private final Map<Integer, Option> options = new HashMap<>();
    private final Scanner scanner = ScannerManager.getInstance();
    private final String PRESENTATION = "OPEN FOOD FACTS";
    public Menu() {
        options.put(1, new ImportFichier());
        options.put(2, new MeilleurPourMarque());
        options.put(3, new MeilleurPourCategorie());
        options.put(4, new MeilleurPourMarqueCategorie());
        options.put(5, new IngredientsCourants());
        options.put(6, new AdditifsCourants());
        options.put(7, new AllergenesCourants());
        options.put(8, new Quitter());
    }

    public void showMenu(){
        String choice;
        boolean continueMenu = true;
        while(continueMenu){
            do{
                System.out.println(PRESENTATION);
                for (int i = 1; i <= options.size(); i++){
                    System.out.println((i) + " : " + options.get(i).getDisplayedCaption());
                }
                System.out.println("----------------------");
                System.out.print("Votre choix : ");
                choice = scanner.nextLine();
            } while (!choice.matches("\\d+") || !options.containsKey(Integer.parseInt(choice)));
            continueMenu = options.get(Integer.parseInt(choice)).executeOption();
        }
    }
}
