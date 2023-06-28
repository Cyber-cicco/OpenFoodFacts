package fr.diginamic.dao;

import fr.diginamic.entites.Produit;

import java.util.List;

public interface ProduitDao extends BaseDao<Produit> {
    List<Produit> extraireNMeilleursParMarque(int n, String marque);
    int getProduitCount();
}
