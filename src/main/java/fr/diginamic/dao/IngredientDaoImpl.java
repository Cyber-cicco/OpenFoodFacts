package fr.diginamic.dao;

import fr.diginamic.config.DatabaseConfig;
import fr.diginamic.entites.Ingredient;

import java.util.List;

public class IngredientDaoImpl extends RepositoryDao implements IngredientDao {

    @Override
    public List<Ingredient> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Ingredient entity) {
        repository.persistEntity(entity);
    }
}
