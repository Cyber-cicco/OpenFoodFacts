package fr.diginamic.dao;

import fr.diginamic.entites.BaseEntity;

import java.util.List;

public interface BaseDao<T extends BaseEntity> {

    List<T> extraire();

    void sauvegarder(T entity);

    void close();
}
