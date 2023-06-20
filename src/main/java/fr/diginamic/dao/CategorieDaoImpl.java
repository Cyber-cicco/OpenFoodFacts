package fr.diginamic.dao;

import fr.diginamic.config.DatabaseConfig;
import fr.diginamic.entites.Categorie;

import java.util.List;

public class CategorieDaoImpl extends RepositoryDao implements CategorieDao {

    @Override
    public List<Categorie> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Categorie entity) {
        repository.persistEntity(entity);
    }
}
