package fr.diginamic.dao;

import fr.diginamic.config.DatabaseConfig;
import fr.diginamic.entites.BaseEntity;
import fr.diginamic.parser.LineParserImpl;
import fr.diginamic.threader.VirtualThread;
import fr.diginamic.types.Producer;
import fr.diginamic.types.RepositoryType;
import lombok.SneakyThrows;

import java.util.*;

/**
 * Classe abstraite de tous les DAOs utilisant le repository
 * pour faire ses requêtes JPA
 * */
public abstract class RepositoryDao<T extends BaseEntity> implements BaseDao<T> {
    /**Objet permettant d'interagir avec la base de données*/
    protected final Repository repository;

    /**
     * Constructeur
     * Instancie une connexion à la base de données à partir du type de la classe avec laquelle
     * interagir.
     * */
    public RepositoryDao(RepositoryType repositoryType) {
        this.repository = Repository.getInstance(DatabaseConfig.DATABASE_NAME, repositoryType);
    }

    /**
     * Méthode permettant de fermer les connexions à la base de données
     * */
    public void close(){
        try {
            repository.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Méthode permettant de persister une liste d'entités dans une seule transaction
     * à partir de threads
     * */
    @SneakyThrows
    public void persistEntities(Set<T> entities, List<Thread> threads){
        List<T> threadEntities = new ArrayList<>(entities);
        Thread thread = VirtualThread.getThread("persistence",  ()-> sauvegarderMultipe(threadEntities));
        threads.add(thread);
        entities.clear();
    }
}
