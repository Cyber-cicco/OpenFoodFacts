package fr.diginamic.dao;

import fr.diginamic.config.DatabaseConfig;
import fr.diginamic.entites.BaseEntity;
import fr.diginamic.parser.LineParserImpl;
import fr.diginamic.threader.VirtualThread;
import fr.diginamic.types.Procedure;
import fr.diginamic.types.RepositoryType;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

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
    protected T getEntity(Procedure<T> constructor, boolean hasToPersist, List<T> entities){
        T entity = constructor.apply();
        entities.add(entity);
        if(hasToPersist){
            List<T> threadEntities = new ArrayList<>(entities);
            Thread thread = VirtualThread.getThread("persistence " + entity.getClass().getSimpleName(), ()-> sauvegarderMultipe(threadEntities));
            LineParserImpl.THREADS.add(thread);
            entities.clear();
        }
        return entity;
    }
}
