package fr.diginamic.dao;

import fr.diginamic.entites.Allergene;
import fr.diginamic.types.Procedure;
import fr.diginamic.types.RepositoryType;

import java.util.List;

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
    public Allergene getAllergene(String nomAllergene, boolean hasToPersist, List<Allergene> allergenes){
        if(allergeneMap.containsKey(nomAllergene)){
            return allergeneMap.get(nomAllergene);
        }
        Procedure<Allergene> constructor = ()->{
            Allergene allergene = new Allergene(nomAllergene);
            allergeneMap.put(nomAllergene, allergene);
            return allergene;
        };
        return getEntity(constructor, hasToPersist, allergenes);
    }
}
