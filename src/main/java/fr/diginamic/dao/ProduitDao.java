package fr.diginamic.dao;

import fr.diginamic.entites.Produit;

import java.util.List;

public interface ProduitDao extends BaseDao<Produit> {
    void extraireNMeilleursParMarque(int n);
    void prepareProduitForPersistence(Produit produit, boolean hasToPersist, List<Produit> entites);
    int getProduitCount();
}
