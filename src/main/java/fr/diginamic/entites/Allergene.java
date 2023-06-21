package fr.diginamic.entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Set;

@Entity
public class Allergene extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;

    public Allergene(String libelle) {
        this.libelle = libelle;
    }

    public Allergene() {
    }
}
