package fr.diginamic.dao;

import fr.diginamic.entites.Marque;
import fr.diginamic.types.RepositoryType;

import java.util.List;

public class MarqueDaoImpl extends RepositoryDao implements MarqueDao{

    public MarqueDaoImpl() {
        super(RepositoryType.MARQUE);
    }
    @Override
    public List<Marque> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Marque entity) {
        repository.persistEntity(entity);
    }

    @Override
    public void sauvegarderMultipe(Marque[] entites) {
        repository.persistMultipleEntites(entites);
    }
}
