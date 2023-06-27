package fr.diginamic.dao;

import fr.diginamic.entites.Allergene;

import java.util.Set;

public interface AllergeneDao extends BaseDao<Allergene> {
    Allergene getAllergene(String nom, Set<Allergene> allergenes);
}
