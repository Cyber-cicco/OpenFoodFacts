package fr.diginamic.entites;

import fr.diginamic.types.ValeurNutritionnelle;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Produit extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Integer energie100g;
    private Integer graisse100g ;
    private Integer sucres100g;
    private Integer fibres100g ;
    private Integer proteines100g ;
    private Integer sel100g;
    private Integer vitA100g;
    private Integer vitD100g;
    private Integer vitE100g;
    private Integer vitK100g;
    private Integer vitC100g;
    private Integer vitB1100g;
    private Integer vitB2100g;
    private Integer vitPP100g;
    private Integer vitB6100g;
    private Integer vitB9100g;
    private Integer vitB12100g;
    private Integer calcium100g;
    private Integer magnesium100g;
    private Integer iron100g;
    private Integer fer100g;
    private Integer betaCarotene100g;
    @Enumerated
    private ValeurNutritionnelle valeurNutritionnelle;

    @ManyToOne
    private Categorie categorie;

    @ManyToMany
    @JoinTable(name = "produit_ingredients",
            joinColumns = @JoinColumn(name = "id_produit", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_ingredient", referencedColumnName = "id")
    )
    private Set<Ingredient> ingredients;

    @ManyToMany
    @JoinTable(name = "produit_allergenes",
        joinColumns = @JoinColumn(name = "id_produit", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_allergene", referencedColumnName = "id")
    )
    private Set<Allergene> allergenes;
    @ManyToMany
    @JoinTable(name = "produit_additifs",
            joinColumns = @JoinColumn(name = "id_produit", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_additif", referencedColumnName = "id")
    )
    private Set<Additif> additifs;
}
