package fr.diginamic.dao;

import fr.diginamic.config.DatabaseConfig;
import fr.diginamic.entites.Allergene;

import java.util.List;

public class AllergeneDaoImpl extends RepositoryDao implements AllergeneDao{
    @Override
    public List<Allergene> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Allergene entity) {
        repository.persistEntity(entity);
    }
}
