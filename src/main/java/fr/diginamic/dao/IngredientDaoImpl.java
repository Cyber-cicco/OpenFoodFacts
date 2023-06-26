package fr.diginamic.dao;

import fr.diginamic.entites.Ingredient;
import fr.diginamic.types.Procedure;
import fr.diginamic.types.RepositoryType;

import java.util.List;

import static fr.diginamic.parser.Cache.ingredientMap;

public class IngredientDaoImpl extends RepositoryDao<Ingredient> implements IngredientDao {

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
    public void sauvegarderMultipe(List<Ingredient> entites) {
        repository.persistMultipleEntites(entites);
    }

    /**
     * Méthode qui va vérifier si un ingrédient existe déjà dans le cache
     * Si c'est le cas, renvoie un ingrédient associé au nom que l'on
     * a passé en paramètre.
     * Sinon, on crée un nouvel ingrédient à partir de son nom,
     * et on l'insère dans le cache, puis en base.
     * @param nomIngredient : nom de l'ingrédient
     * @return Ingredient
     * */
    public Ingredient getIngredient(String nomIngredient, boolean hasToPersist, List<Ingredient> ingredients){
        if(ingredientMap.containsKey(nomIngredient)){
            return ingredientMap.get(nomIngredient);
        }
        Procedure<Ingredient> constructor = ()->{
            Ingredient ingredient = new Ingredient(nomIngredient);
            ingredientMap.put(nomIngredient, ingredient);
            return ingredient;
        };
        return getEntity(constructor, hasToPersist, ingredients);
    }
}
