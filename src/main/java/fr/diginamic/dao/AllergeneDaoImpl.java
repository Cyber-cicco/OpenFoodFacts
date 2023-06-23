package fr.diginamic.dao;

import fr.diginamic.entites.Allergene;
import fr.diginamic.types.RepositoryType;

import java.util.List;

public class AllergeneDaoImpl extends RepositoryDao implements AllergeneDao{

    public AllergeneDaoImpl() {
        super(RepositoryType.ALLERGENE);
    }
    @Override
    public List<Allergene> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Allergene entity) {
        repository.persistEntity(entity);
    }

    @Override
    public void sauvegarderMultipe(Allergene[] entites) {
        repository.persistMultipleEntites(entites);
    }
}
