package fr.diginamic.dao;

import fr.diginamic.entites.Produit;

import java.util.List;

public interface ProduitDao extends BaseDao<Produit> {
    void extraireNMeilleursParMarque(int n);
    int getProduitCount();
}
