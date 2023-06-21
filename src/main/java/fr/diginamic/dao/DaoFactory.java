package fr.diginamic.dao;

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
        if(produitDao != null) return marqueDao;
        marqueDao =  new MarqueDaoImpl();
        return marqueDao;
    }
}
