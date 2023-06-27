package fr.diginamic.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Marque extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;

    public Marque(String nom) {
        this.nom = nom;
    }

    public Marque() {
    }

    @Override
    public String toString() {
        return "Marque{" +
                "nom='" + nom + '\'' +
                '}';
    }

    @Override
    public String getCacheId() {
        return nom;
    }
}