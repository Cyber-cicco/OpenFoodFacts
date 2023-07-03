package fr.diginamic.menu.options;

import fr.diginamic.dao.DaoFactory;
import fr.diginamic.dao.IngredientDao;
import fr.diginamic.utils.ScannerManager;

import java.util.List;
import java.util.Scanner;

public class IngredientsCourants extends Option{
    private final Scanner scanner = ScannerManager.getInstance();
    private final IngredientDao ingredientDao = DaoFactory.getIngredientDao();
    public IngredientsCourants() {
        super("Afficher les N ingrédients les plus courants");
    }

    @Override
    public boolean executeOption() {
        String choixNombre;
        do {
            System.out.print("Combien d'ingrédients souhaitez-vous afficher? : ");
            choixNombre = scanner.nextLine();
        } while (!choixNombre.matches("\\d+"));
        List<Object[]>produits =  ingredientDao.getIngredientsCourants(Integer.parseInt(choixNombre));
        produits.forEach(o -> System.out.println(o[0] + " : " + o[1] + " occurences"));
        return true;
    }
}
