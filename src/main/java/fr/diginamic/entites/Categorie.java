package fr.diginamic.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class Categorie extends BaseEntity {

    private String libelle;
    @OneToMany(mappedBy = "categorie")
    private Set<Produit> produits;
}
