package fr.diginamic.dao;

import fr.diginamic.entites.Ingredient;

import java.util.List;

public interface IngredientDao extends BaseDao<Ingredient> {
    Ingredient getIngredient(String nomIngredient, boolean hasToPersist, List<Ingredient> ingredients);
}
