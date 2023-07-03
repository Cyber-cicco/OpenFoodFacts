package fr.diginamic.dao;

import fr.diginamic.entites.Ingredient;
import fr.diginamic.types.RepositoryType;

import java.util.List;
import java.util.Set;

import static fr.diginamic.parser.Cache.ingredientMap;

public class IngredientDaoImpl extends RepositoryDao<Ingredient> implements IngredientDao {

    /**
     * Constructeur
     * Initialise la connexion à la base de données en donnant le type ingredient
     * */
    public IngredientDaoImpl() {
        super(RepositoryType.INGREDIENT);
    }

    @Override
    public List<Ingredient> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Ingredient entity) {
        repository.persistEntityWithNewConnection(entity);
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
    public Ingredient getIngredient(String nomIngredient, Set<Ingredient> ingredients){
        boolean containsKey = ingredientMap.containsKey(nomIngredient);
        Ingredient ingredient = (containsKey) ? ingredientMap.get(nomIngredient) : new Ingredient(nomIngredient);
        if(!containsKey){
            ingredientMap.put(nomIngredient, ingredient);
            ingredients.add(ingredient);
        }
        return ingredient;
    }

    public List<Object[]> getIngredientsCourants(int nbIngredients){
        String nativeQuery = String.format("select i.nom, count(i.nom) as nb_produit from Produit p left join produit_ingredients pi on p.id = pi.id_produit left join Ingredient i on pi.id_ingredient = i.id group by i.nom order by nb_produit DESC limit %s;", nbIngredients);
        return repository.executeNativeQuery(nativeQuery);
    }
}
