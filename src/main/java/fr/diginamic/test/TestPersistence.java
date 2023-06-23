package fr.diginamic.test;

import fr.diginamic.dao.*;
import fr.diginamic.entites.Additif;

/**
 * Classe de teste pour les daos
 * */
public class TestPersistence {

    public static void main(String[] args) {
        ProduitDao produitDao = DaoFactory.getProduitDao();
        AdditifDao additifDao = DaoFactory.getAdditifDao();
        CategorieDao categorieDao = DaoFactory.getCategorieDao();
        IngredientDao ingredientDao = DaoFactory.getIngredientDao();
        AllergeneDao allergeneDao = DaoFactory.getAllergeneDao();
        Additif additif = new Additif("EAC", "Addictif additif");
        additifDao.sauvegarder(additif);
        produitDao.close();
    }
}
