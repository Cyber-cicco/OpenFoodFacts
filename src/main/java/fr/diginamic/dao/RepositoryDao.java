package fr.diginamic.dao;

import fr.diginamic.config.DatabaseConfig;
import fr.diginamic.types.RepositoryType;

public abstract class RepositoryDao {
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
}
