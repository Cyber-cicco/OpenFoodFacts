package fr.diginamic.menu.options;

import fr.diginamic.dao.DaoFactory;
import fr.diginamic.utils.ScannerManager;

import java.util.Scanner;

public class Quitter extends Option{
    private final Scanner scanner = ScannerManager.getInstance();
    public Quitter() {
        super("quitter l'application");
    }

    @Override
    public boolean executeOption() {
        //On ferme toutes les connexions ouvertes par les DAOS
        DaoFactory.closeDaos();
        scanner.close();
        return false;
    }
}
