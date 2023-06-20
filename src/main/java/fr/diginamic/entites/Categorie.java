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
}
