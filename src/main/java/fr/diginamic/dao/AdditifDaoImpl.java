package fr.diginamic.dao;

import fr.diginamic.entites.Additif;
import fr.diginamic.types.Producer;
import fr.diginamic.types.RepositoryType;

import java.util.List;
import java.util.Set;

import static fr.diginamic.parser.Cache.additifMap;

public class AdditifDaoImpl extends RepositoryDao<Additif> implements AdditifDao{
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
    public void sauvegarderMultipe(List<Additif> entites) {
        repository.persistMultipleEntites(entites);
    }

    /**
     * Méthode qui va vérifier si un additif existe déjà dans le cache
     * Si c'est le cas, renvoie un additif associé au code que l'on
     * a passé en paramètre.
     * Sinon, on crée un nouvel additif à partir de son nom et de son code,
     * et on l'insère dans le cache, puis en base.
     * @param code : code unique de l'additif
     * @param nom : nom de l'additif
     * @return Additif
     * */
    @Override
    public Additif getAdditif(String code, String nom, Set<Additif> additifs){
        boolean containsKey = additifMap.containsKey(code);
        Additif additif = (containsKey) ? additifMap.get(code) : new Additif(code, nom);
        if(!containsKey){
            additifMap.put(code, additif);
            additifs.add(additif);
        }
        return additif;
    }
}
