package fr.diginamic.dao;

import fr.diginamic.entites.Produit;
import fr.diginamic.types.RepositoryType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProduitDaoImpl extends RepositoryDao<Produit> implements ProduitDao {

    private int produitCount;

    public ProduitDaoImpl() {
        super(RepositoryType.PRODUIT);
    }

    @Override
    public List<Produit> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Produit entity) {
        repository.persistEntity(entity);
    }

    @Override
    public void sauvegarderMultipe(List<Produit> entites) {
        repository.persistMultipleEntites(entites);
    }

    public void extraireNMeilleursParMarque(int n){
        String query = "select p from Produit p where p.valeurNutritionnelle = 0 and p.marque.nom = 'chabrior'";
        Map<String, String> args = new HashMap<>();
        System.out.println(repository.executeQuery(query, args, n));
    }

    @Override
    public int getProduitCount() {
        return produitCount;
    }

}
