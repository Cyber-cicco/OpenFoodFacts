package fr.diginamic.dao;

import fr.diginamic.config.DatabaseConfig;
import fr.diginamic.entites.Additif;

import java.util.List;

public class AdditifDaoImpl extends RepositoryDao implements AdditifDao{
    @Override
    public List<Additif> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Additif entity) {
        repository.persistEntity(entity);
    }
}
