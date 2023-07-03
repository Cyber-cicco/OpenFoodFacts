package fr.diginamic.menu.options;

import fr.diginamic.dao.DaoFactory;
import fr.diginamic.dao.AllergeneDao;
import fr.diginamic.utils.ScannerManager;

import java.util.List;
import java.util.Scanner;

public class AllergenesCourants extends Option {
    private final Scanner scanner = ScannerManager.getInstance();
    private final AllergeneDao allergeneDao = DaoFactory.getAllergeneDao();
    public AllergenesCourants() {
        super("Afficher les N allergenes les plus courants");
    }

    @Override
    public boolean executeOption() {
        String choixNombre;
        do {
            System.out.print("Combien d'allergenes souhaitez-vous afficher? : ");
            choixNombre = scanner.nextLine();
        } while (!choixNombre.matches("\\d+"));
        List<Object[]> produits =  allergeneDao.getAllergenesCourants(Integer.parseInt(choixNombre));
        produits.forEach(o -> System.out.println(o[0] + " : " + o[1] + " occurences"));
        return true;
    }
}
