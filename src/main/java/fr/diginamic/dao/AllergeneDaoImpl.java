package fr.diginamic.dao;

import fr.diginamic.entites.Allergene;
import fr.diginamic.types.Producer;
import fr.diginamic.types.RepositoryType;

import java.util.List;
import java.util.Set;

import static fr.diginamic.parser.Cache.allergeneMap;

public class AllergeneDaoImpl extends RepositoryDao<Allergene> implements AllergeneDao{

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
    public void sauvegarderMultipe(List<Allergene> entites) {
        repository.persistMultipleEntites(entites);
    }

    /**
     * Méthode qui va vérifier si un allergène existe déjà dans le cache
     * Si c'est le cas, renvoie un allergène associé au nom que l'on
     * a passé en paramètre.
     * Sinon, on crée un nouvel allergène à partir de son nom,
     * et on l'insère dans le cache, puis en base.
     * @param nomAllergene : nom de l'allergene
     * @return Allergene
     * */
    public Allergene getAllergene(String nomAllergene, Set<Allergene> allergenes){
        boolean containsKey = allergeneMap.containsKey(nomAllergene);
        Allergene allergene = (containsKey) ? allergeneMap.get(nomAllergene) : new Allergene(nomAllergene);
        if(!containsKey){
            allergeneMap.put(nomAllergene, allergene);
            allergenes.add(allergene);
        }
        return allergene;
    }
}
