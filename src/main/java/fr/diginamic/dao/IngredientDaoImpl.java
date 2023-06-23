package fr.diginamic.dao;

import fr.diginamic.entites.Ingredient;
import fr.diginamic.types.RepositoryType;

import java.util.List;

public class IngredientDaoImpl extends RepositoryDao implements IngredientDao {

    public IngredientDaoImpl() {
        super(RepositoryType.INGREDIENT);
    }

    @Override
    public List<Ingredient> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Ingredient entity) {
        repository.persistEntity(entity);
    }

    @Override
    public void sauvegarderMultipe(Ingredient[] entites) {
        repository.persistMultipleEntites(entites);
    }
}
