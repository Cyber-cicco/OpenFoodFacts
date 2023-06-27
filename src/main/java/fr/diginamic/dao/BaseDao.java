package fr.diginamic.dao;

import fr.diginamic.entites.BaseEntity;
import fr.diginamic.types.Producer;

import java.util.List;
import java.util.Set;

public interface BaseDao<T extends BaseEntity> {

    List<T> extraire();
    void persistEntities(Set<T> entities, List<Thread> threads);

    void sauvegarder(T entity);

    void sauvegarderMultipe(List<T> entites);

    void close();
}
