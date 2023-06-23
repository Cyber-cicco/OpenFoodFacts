package fr.diginamic.dao;

import fr.diginamic.entites.Additif;
import fr.diginamic.types.RepositoryType;

import java.util.List;

public class AdditifDaoImpl extends RepositoryDao implements AdditifDao{
    public AdditifDaoImpl() {
        super(RepositoryType.ADDITIF);
    }

    @Override
    public List<Additif> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Additif entity) {
        repository.persistEntity(entity);
    }

    @Override
    public void sauvegarderMultipe(Additif[] entites) {
        repository.persistMultipleEntites(entites);
    }
}
