package fr.diginamic.dao;

import fr.diginamic.entites.Allergene;

import java.util.List;

public interface AllergeneDao extends BaseDao<Allergene> {
    Allergene getAllergene(String nom, boolean hasToPersist, List<Allergene> allergenes);
}
