package fr.diginamic.entites;

import jakarta.persistence.*;

@Entity
public class Ingredient extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(1000)")
    private String nom;
    public Ingredient(String nom) {
        this.nom = nom;
    }

    public Ingredient() {
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }

    @Override
    public String getCacheId() {
        return nom;
    }
}
