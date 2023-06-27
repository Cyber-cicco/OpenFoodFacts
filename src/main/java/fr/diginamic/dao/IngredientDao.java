package fr.diginamic.dao;

import fr.diginamic.entites.Ingredient;

import java.util.Set;

public interface IngredientDao extends BaseDao<Ingredient> {
    Ingredient getIngredient(String nomIngredient, Set<Ingredient> ingredients);
}
