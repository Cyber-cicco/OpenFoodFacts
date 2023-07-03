package fr.diginamic.dao;

import fr.diginamic.entites.Produit;
import fr.diginamic.exception.EntityNotFoundException;
import fr.diginamic.types.RepositoryType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProduitDaoImpl extends RepositoryDao<Produit> implements ProduitDao {

    /**
     * Constructeur
     * Initialise la connexion à la base de données en donnant le type produit
     * */
    public ProduitDaoImpl() {
        super(RepositoryType.PRODUIT);
    }

    @Override
    public List<Produit> extraire() {
        return null;
    }

    @Override
    public void sauvegarder(Produit entity) {
        repository.persistEntityWithNewConnection(entity);
    }

    @Override
    public void sauvegarderMultipe(List<Produit> entites) {
        repository.persistMultipleEntites(entites);
    }

    public List<Produit> extraireNMeilleursParMarque(int n, String marque){
        marque = marque.toLowerCase();
        try{
            String query = "select m from Marque m where m.nom = :nom";
            Map<String, String> args = new HashMap<>();
            args.put("nom", marque);
            if(repository.findByField(query, args).size() == 0){
                throw new EntityNotFoundException("la catégorie saisie semble ne pas exister.");
            }

        } catch (EntityNotFoundException e){
            System.out.println("Erreur dans la requête : " + e.getMessage());
        }
        String query = "select p from Produit p where p.marque.nom = :marque order by p.valeurNutritionnelle";
        Map<String, String> args = new HashMap<>();
        args.put("marque", marque);
        return repository.executeQuery(query, args, n);
    }

    @Override
    public List<Produit> extraireNMeilleursParCategorie(int n, String categorie) {
        categorie = categorie.toLowerCase();
        try{
            String query = "select c from Categorie c where c.libelle = :libelle";
            Map<String, String> args = new HashMap<>();
            args.put("libelle", categorie);
            if(repository.findByField(query, args).size() == 0){
                throw new EntityNotFoundException("la catégorie saisie semble ne pas exister.");
            }

        } catch (EntityNotFoundException e){
            System.out.println("Erreur dans la requête : " + e.getMessage());
        }
        String query = "select p from Produit p where p.categorie.libelle = :categorie order by p.valeurNutritionnelle";
        Map<String, String> args = new HashMap<>();
        args.put("categorie", categorie);
        return repository.executeQuery(query, args, n);
    }

    @Override
    public List<Produit> extraireNMeilleursParCategorieEtMarque(int nbProduits, String nomCategorie, String nomMarque) {
        nomCategorie = nomCategorie.toLowerCase();
        nomMarque = nomMarque.toLowerCase();
        try{
            String queryCategorie = "select c from Categorie c where c.libelle = :libelle";
            String queryMarque = "select m from Marque m where m.nom = :libelle";
            Map<String, String> args = new HashMap<>();
            args.put("libelle", nomCategorie);
            if(repository.findByField(queryCategorie, args).size() == 0){
                throw new EntityNotFoundException("la catégorie saisie semble ne pas exister.");
            }
            args.put("libelle", nomMarque);
            if(repository.findByField(queryMarque, args).size() == 0){
                throw new EntityNotFoundException("la marque saisie semble ne pas exister.");
            }
        } catch (EntityNotFoundException e){
            System.out.println("Erreur dans la requête : " + e.getMessage());
        }
        String query = "select p from Produit p where p.categorie.libelle = :categorie and p.marque.nom = :marque order by p.valeurNutritionnelle";
        Map<String, String> args = new HashMap<>();
        args.put("categorie", nomCategorie);
        args.put("marque", nomMarque);
        return repository.executeQuery(query, args, nbProduits);
    }

}
