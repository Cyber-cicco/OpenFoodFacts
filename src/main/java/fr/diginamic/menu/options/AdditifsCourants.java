package fr.diginamic.menu.options;

import fr.diginamic.dao.AdditifDao;
import fr.diginamic.dao.DaoFactory;
import fr.diginamic.utils.ScannerManager;

import java.util.List;
import java.util.Scanner;

public class AdditifsCourants extends Option{
    private final Scanner scanner = ScannerManager.getInstance();
    private final AdditifDao additifDao = DaoFactory.getAdditifDao();
    public AdditifsCourants() {
        super("Afficher les N additifs les plus courants");
    }

    @Override
    public boolean executeOption() {
        String choixNombre;
        do {
            System.out.print("Combien d'additifs souhaitez-vous afficher? : ");
            choixNombre = scanner.nextLine();
        } while (!choixNombre.matches("\\d+"));
        List<Object[]> produits =  additifDao.getAdditifsCourants(Integer.parseInt(choixNombre));
        produits.forEach(o -> System.out.println(o[0] + " : " + o[1] + " occurences"));
        return true;
    }
}
