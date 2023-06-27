package fr.diginamic.config;
/**Classe contenant des variables régissant les interactions avec la BDD*/
public class DatabaseConfig {

    /**Nom de la BDD*/
    public static final String DATABASE_NAME = "openFoodFacts";
    /**Nombre maximum de produits à persister d'un seul tenant dans la BDD*/
    public static final int MAX_PERSISTENCE = 500;
}
