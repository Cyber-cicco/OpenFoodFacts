package fr.diginamic.dao;

import fr.diginamic.entites.Marque;

import java.util.List;

public interface MarqueDao extends BaseDao<Marque>{
    Marque getMarque(String nomMarque, boolean hasToPersist, List<Marque> marques);
}
