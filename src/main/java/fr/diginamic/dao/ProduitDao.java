package fr.diginamic.dao;

import fr.diginamic.entites.Produit;

public interface ProduitDao extends BaseDao<Produit> {
    void extraireNMeilleursParMarque(int n);
}
