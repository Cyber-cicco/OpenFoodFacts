package fr.diginamic.dao;

import fr.diginamic.config.DatabaseConfig;
import fr.diginamic.entites.BaseEntity;
import fr.diginamic.parser.LineParserImpl;
import fr.diginamic.threader.VirtualThread;
import fr.diginamic.types.Producer;
import fr.diginamic.types.RepositoryType;
import lombok.SneakyThrows;

import java.util.*;

public abstract class RepositoryDao<T extends BaseEntity> implements BaseDao<T> {
    protected final Repository repository;

    public RepositoryDao(RepositoryType repositoryType) {
        this.repository = Repository.getInstance(DatabaseConfig.DATABASE_NAME, repositoryType);
    }

    public void close(){
        try {
            repository.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @SneakyThrows
    public void persistEntities(Set<T> entities, List<Thread> threads){
        List<T> threadEntities = new ArrayList<>(entities);
        Thread thread = VirtualThread.getThread("persistence",  ()-> sauvegarderMultipe(threadEntities));
        threads.add(thread);
        entities.clear();
    }
}
