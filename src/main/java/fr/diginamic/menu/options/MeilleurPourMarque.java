package fr.diginamic.menu.options;

import fr.diginamic.dao.*;
import fr.diginamic.entites.Produit;
import fr.diginamic.utils.ScannerManager;

import java.util.List;
import java.util.Scanner;

/**
 * Classe implémentant les fonctionnalités de l'option
 * N meilleures pour la marque du menu
 * */
public class MeilleurPourMarque extends Option{
    private ProduitDao dao = DaoFactory.getProduitDao();

    private Scanner scanner = ScannerManager.getInstance();
    @Override
    public boolean executeOption() {
        System.out.print("Tappez le nom de la marque dont vous souhaitez voir les meilleures produits : ");
        String nomMarque = scanner.nextLine();
        String choixNombre;
        do {
            System.out.println("Combien de produits souhaitez-vous afficher? : ");
            choixNombre = scanner.nextLine();
        } while (!choixNombre.matches("\\d+"));
        int nbProduits = Integer.parseInt(choixNombre);
        List<Produit> produits =  dao.extraireNMeilleursParMarque(nbProduits, nomMarque);
        produits.forEach(System.out::println);
        return true;
    }

    public MeilleurPourMarque() {
        super("montrer les N meilleurs produit d'une marque donnée");
    }
}
