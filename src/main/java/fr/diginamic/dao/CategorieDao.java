package fr.diginamic.dao;

import fr.diginamic.entites.Categorie;

import java.util.List;

public interface CategorieDao extends BaseDao<Categorie> {
    Categorie getCategorie(String nomCategorie, boolean hasToPersist, List<Categorie> categories);
}
