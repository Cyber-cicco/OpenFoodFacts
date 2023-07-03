package fr.diginamic.dao;

/**
 * Classe contenant les règles de création de chaque DAO
 * */
public class DaoFactory {

    private static AdditifDao additifDao;
    private static AllergeneDao allergeneDao;
    private static CategorieDao categorieDao;
    private static IngredientDao ingredientDao;
    private static ProduitDao produitDao;
    private static MarqueDao marqueDao;

    public static AdditifDao getAdditifDao(){
        if(additifDao != null) return additifDao;
        additifDao = new AdditifDaoImpl();
        return additifDao;
    }
    public static AllergeneDao getAllergeneDao(){
        if(allergeneDao != null) return allergeneDao;
        allergeneDao = new AllergeneDaoImpl();
        return allergeneDao;
    }
    public static CategorieDao getCategorieDao(){
        if(categorieDao != null) return categorieDao;
        categorieDao = new CategorieDaoImpl();
        return categorieDao;
    }
    public static IngredientDao getIngredientDao(){
        if(ingredientDao != null) return ingredientDao;
        ingredientDao = new IngredientDaoImpl();
        return ingredientDao;
    }
    public static ProduitDao getProduitDao(){
        if(produitDao != null) return produitDao;
        produitDao =  new ProduitDaoImpl();
        return produitDao;
    }
    public static MarqueDao getMarqueDao(){
        if(marqueDao != null) return marqueDao;
        marqueDao =  new MarqueDaoImpl();
        return marqueDao;
    }
    /**
     * Méthode permettant de fermer
     * les connexions à la base de données.
     * */
    public static void closeDaos(){
        if(additifDao != null) additifDao.close();
        if(allergeneDao != null) allergeneDao.close();
        if(ingredientDao != null) ingredientDao.close();
        if(produitDao != null) produitDao.close();
        if(marqueDao != null) marqueDao.close();
        if(categorieDao != null) categorieDao.close();
    }
}
