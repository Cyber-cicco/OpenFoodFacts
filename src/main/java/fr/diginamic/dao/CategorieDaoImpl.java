package fr.diginamic.dao;

import fr.diginamic.entites.Categorie;
import fr.diginamic.types.RepositoryType;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Set;

import static fr.diginamic.parser.Cache.categorieMap;

public class CategorieDaoImpl extends RepositoryDao<Categorie> implements CategorieDao {


    public CategorieDaoImpl() {
        super(RepositoryType.CATEGORIE);
    }

    @Override
    public List<Categorie> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Categorie entity) {
        repository.persistEntity(entity);
    }

    @Override
    public void sauvegarderMultipe(List<Categorie> entites) {
        repository.persistMultipleEntites(entites);
    }

    /**
     * Méthode qui va vérifier si une catégorie existe déjà dans le cache
     * Si c'est le cas, renvoie une catégorie associée au nom que l'on
     * a passé en paramètre.
     * Sinon, on crée une nouvelle catégore à partir de son nom,
     * et on l'insère dans le cache, puis en base.
     * @param nomCategorie : le nom de la catégorie
     * @return Categorie
     * */
    @SneakyThrows
    public Categorie getCategorie(String nomCategorie, Set<Categorie> categories){
        boolean containsKey = categorieMap.containsKey(nomCategorie);
        Categorie categorie = (containsKey) ? categorieMap.get(nomCategorie) : new Categorie(nomCategorie);
        if(!containsKey){
            categorieMap.put(nomCategorie, categorie);
            categories.add(categorie);
        }
        return categorie;
    }
}
