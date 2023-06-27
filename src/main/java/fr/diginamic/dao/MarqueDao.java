package fr.diginamic.dao;

import fr.diginamic.entites.Marque;

import java.util.List;
import java.util.Set;

public interface MarqueDao extends BaseDao<Marque>{
    Marque getMarque(String nomMarque, boolean hasToPersist, Set<Marque> marques);
}
