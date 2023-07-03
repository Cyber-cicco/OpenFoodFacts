package fr.diginamic.menu.options;

import fr.diginamic.dao.ProduitDao;
import fr.diginamic.dao.ProduitDaoImpl;
import fr.diginamic.entites.Produit;
import fr.diginamic.utils.ScannerManager;

import java.util.List;
import java.util.Scanner;

public class MeilleurPourMarqueCategorie extends Option{
    private final ProduitDao dao = new ProduitDaoImpl();
    private final Scanner scanner = ScannerManager.getInstance();
    public MeilleurPourMarqueCategorie() {
        super("montrer les N meilleurs produits par marque et catégorie");
    }

    @Override
    public boolean executeOption() {
        System.out.print("Tappez le nom de la marque dont vous souhaitez voir les meilleures produits : ");
        String nomMarque = scanner.nextLine();
        System.out.print("Tappez le nom de la catégorie dont vous souhaitez voir les meilleures produits : ");
        String nomCategorie = scanner.nextLine();
        String choixNombre;
        do {
            System.out.print("Combien de produits souhaitez-vous afficher? : ");
            choixNombre = scanner.nextLine();
        } while (!choixNombre.matches("\\d+"));
        int nbProduits = Integer.parseInt(choixNombre);
        List<Produit> produits =  dao.extraireNMeilleursParCategorieEtMarque(nbProduits, nomCategorie, nomMarque);
        produits.forEach(System.out::println);
        return true;
    }
}
