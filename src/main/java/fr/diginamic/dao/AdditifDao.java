package fr.diginamic.dao;

import fr.diginamic.entites.Additif;

import java.util.Set;

public interface AdditifDao extends BaseDao<Additif>{

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
    Additif getAdditif(String code, String nom, Set<Additif> additifs);
}
