package fr.diginamic.dao;

import fr.diginamic.entites.Marque;
import fr.diginamic.types.Producer;
import fr.diginamic.types.RepositoryType;

import java.util.List;
import java.util.Set;

import static fr.diginamic.parser.Cache.marqueMap;

public class MarqueDaoImpl extends RepositoryDao<Marque> implements MarqueDao{

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
    public void sauvegarderMultipe(List<Marque> entites) {
        repository.persistMultipleEntites(entites);
    }

    /**
     * Méthode qui va vérifier si une marque existe déjà dans le cache
     * Si c'est le cas, renvoie une marque associée au nom que l'on
     * a passé en paramètre.
     * Sinon, on crée une nouvelle marque à partir de son nom,
     * et on l'insère dans le cache, puis en base.
     * @param nomMarque : nom de la marque
     * @return Marque
     * */
    public Marque getMarque(String nomMarque, boolean hasToPersist, Set<Marque> marques){
        boolean containsKey = marqueMap.containsKey(nomMarque);
        Marque marque = (containsKey) ? marqueMap.get(nomMarque) : new Marque(nomMarque);
        if(!containsKey){
            marqueMap.put(nomMarque, marque);
            marques.add(marque);
        }
        return marque;
    }
}
