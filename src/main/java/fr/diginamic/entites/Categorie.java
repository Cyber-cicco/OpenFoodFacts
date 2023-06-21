package fr.diginamic.entites;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Categorie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    @OneToMany(mappedBy = "categorie")
    private Set<Produit> produits;

    public Categorie() {
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "libelle='" + libelle + '\'' +
                '}';
    }

    public Categorie(String libelle) {
        this.libelle = libelle;
    }
}
