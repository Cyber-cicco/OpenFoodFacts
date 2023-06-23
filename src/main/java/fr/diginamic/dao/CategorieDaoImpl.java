package fr.diginamic.dao;

import fr.diginamic.entites.Categorie;
import fr.diginamic.types.RepositoryType;

import java.util.List;

public class CategorieDaoImpl extends RepositoryDao implements CategorieDao {

    public CategorieDaoImpl() {
        super(RepositoryType.CATEGORIE);
    }

    @Override
    public List<Categorie> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Categorie entity) {
        repository.persistEntity(entity);
    }

    @Override
    public void sauvegarderMultipe(Categorie[] entites) {
        repository.persistMultipleEntites(entites);
    }
}
