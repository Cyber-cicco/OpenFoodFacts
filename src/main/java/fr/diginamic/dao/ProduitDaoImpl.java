package fr.diginamic.dao;

import fr.diginamic.config.DatabaseConfig;
import fr.diginamic.entites.Produit;

import java.util.List;

public class ProduitDaoImpl extends RepositoryDao implements ProduitDao {

    @Override
    public List<Produit> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Produit entity) {
        repository.persistEntity(entity);
    }
}
