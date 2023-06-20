package fr.diginamic.dao;

import fr.diginamic.config.DatabaseConfig;

public abstract class RepositoryDao {
    protected final Repository repository;
    public RepositoryDao() {
        this.repository = Repository.getInstance(DatabaseConfig.DATABASE_NAME);
    }

    public void close(){
        try {
            repository.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
