package fr.diginamic.dao;

import fr.diginamic.entites.Allergene;

import java.util.List;
import java.util.Set;

/**Contrat sur les persistences des Allergenes*/
public interface AllergeneDao extends BaseDao<Allergene> {
    /**Un DAO d'allergene doit pouvoir retourner un allergene du cache ou en créer un nouveau*/
    Allergene getAllergene(String nom, Set<Allergene> allergenes);
     List<Object[]> getAllergenesCourants(int nbAllergenes);
}
