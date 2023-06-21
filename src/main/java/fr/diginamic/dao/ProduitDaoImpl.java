package fr.diginamic.dao;

import fr.diginamic.entites.Produit;
import fr.diginamic.types.RepositoryType;

import java.util.List;

public class ProduitDaoImpl extends RepositoryDao implements ProduitDao {

    public ProduitDaoImpl() {
        super(RepositoryType.PRODUIT);
    }

    @Override
    public List<Produit> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Produit entity) {
        repository.persistEntity(entity);
    }
}
