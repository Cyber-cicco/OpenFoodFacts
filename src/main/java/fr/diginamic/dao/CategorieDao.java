package fr.diginamic.dao;

import fr.diginamic.entites.Categorie;

import java.util.Set;

public interface CategorieDao extends BaseDao<Categorie> {
    Categorie getCategorie(String nomCategorie, Set<Categorie> categories);
}
