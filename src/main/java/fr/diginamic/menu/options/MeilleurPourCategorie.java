package fr.diginamic.menu.options;

import fr.diginamic.dao.DaoFactory;
import fr.diginamic.dao.ProduitDao;
import fr.diginamic.entites.Produit;
import fr.diginamic.utils.ScannerManager;

import java.util.List;
import java.util.Scanner;

public class MeilleurPourCategorie extends Option{
    private Scanner scanner = ScannerManager.getInstance();

    private ProduitDao dao = DaoFactory.getProduitDao();
    public MeilleurPourCategorie() {
        super("montrer les N meilleurs produit d'une catégorie donnée");
    }

    @Override
    public boolean executeOption() {
        System.out.print("Tappez le nom de la catégorie dont vous souhaitez voir les meilleures produits : ");
        String nomCategorie = scanner.nextLine();
        String choixNombre;
        do {
            System.out.print("Combien de produits souhaitez-vous afficher? : ");
            choixNombre = scanner.nextLine();
        } while (!choixNombre.matches("\\d+"));
        int nbProduits = Integer.parseInt(choixNombre);
        List<Produit> produits =  dao.extraireNMeilleursParCategorie(nbProduits, nomCategorie);
        produits.forEach(System.out::println);
        return true;
    }
}
