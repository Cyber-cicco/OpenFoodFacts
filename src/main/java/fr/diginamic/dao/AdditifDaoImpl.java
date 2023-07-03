package fr.diginamic.dao;

import fr.diginamic.entites.Additif;
import fr.diginamic.types.RepositoryType;

import java.util.List;
import java.util.Set;

import static fr.diginamic.parser.Cache.additifMap;

/**
 * Classe contenant la logique métier pour interagir avec la table des additifs
 * dans la base de données
 * */
public class AdditifDaoImpl extends RepositoryDao<Additif> implements AdditifDao{
    /**
     * Constructeur
     * Initialise la connexion à la base de données en donnant le type additif
     * */
    public AdditifDaoImpl() {
        super(RepositoryType.ADDITIF);
    }

    @Override
    public List<Additif> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Additif entity) {
        repository.persistEntityWithNewConnection(entity);
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
     * et on l'insère dans le cache.
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
    public List<Object[]> getAdditifsCourants(int nbAdditifs){
        String nativeQuery = String.format(" select a.libelle, count(a.libelle) as nb_additifs from Produit p left join produit_additifs pa on p.id = pa.id_produit left join Additif a on pa.id_additif = a.id group by a.libelle order by nb_additifs DESC limit %s;", nbAdditifs);
        return repository.executeNativeQuery(nativeQuery);
    }
}
