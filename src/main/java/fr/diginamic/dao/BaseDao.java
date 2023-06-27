package fr.diginamic.dao;

import fr.diginamic.entites.BaseEntity;

import java.util.List;
import java.util.Set;

/**
 * Interface fixant les méthodes minimales à avoir pour tout DAO
 * */
public interface BaseDao<T extends BaseEntity> {

    /**Récupère toutes les données*/
    List<T> extraire();
    /**Met une liste d'entités dans un thread pour la persister*/
    void persistEntities(Set<T> entities, List<Thread> threads);

    /**Persiste une entité*/
    void sauvegarder(T entity);

    /**Persiste une liste d'entités en base*/
    void sauvegarderMultipe(List<T> entites);

    /**Permet de fermer les connexions à la BDD du DAO*/
    void close();
}
