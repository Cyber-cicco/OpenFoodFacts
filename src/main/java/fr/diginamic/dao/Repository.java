package fr.diginamic.dao;

import fr.diginamic.types.RepositoryType;
import jakarta.persistence.*;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Repository implements AutoCloseable{

    private final EntityManager em;
    private final EntityManagerFactory emf;
    private final String SELECT = "select e from %s e";
    private final RepositoryType repositoryType;
    private final String SELECT_WITH_CONDITION = "select e from %s e where %s = %s";
    private static List<Repository> generalRepositores = new ArrayList<>();

    private final String dataBaseName;
    private Repository(String dataBaseName, RepositoryType repositoryType){
        emf = Persistence.createEntityManagerFactory(dataBaseName);
        em = emf.createEntityManager();
        this.repositoryType = repositoryType;
        this.dataBaseName = dataBaseName;
    }

    public static Repository getInstance(String dataBaseName, RepositoryType repositoryType){
        List<Repository> repository = generalRepositores.stream()
                .filter(generalRepository -> generalRepository.getDataBaseName()  == dataBaseName && generalRepository.repositoryType == repositoryType).toList();
        if(repository.size() > 0){
            return repository.get(0);
        }
        var newRepo = new Repository(dataBaseName, repositoryType);
        generalRepositores.add(newRepo);
        System.out.println(newRepo);
        return newRepo;
    }

    public <T> void persistEntity(T entity){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        em.persist(entity);
        tr.commit();
        em.close();
    }

    public <T> void doTransaction(T entity, Consumer<T> transaction){
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        transaction.accept(entity);
        tr.commit();
        em.close();
    }

    public <T> List<T> getAll(String entityName){
        Query query =  em.createQuery(String.format(SELECT, entityName));
        return  query.getResultList();
    }

    public Object findByField(String entityName, String field, String fieldValue){
        Query query =  em.createQuery(String.format(SELECT_WITH_CONDITION, entityName, field, fieldValue));
        return query.getResultList();
    }
    public <T> void changeEntity(Consumer<T> changeEntity, T entity, int id){
        T managedEntity = (T) em.find(entity.getClass(), id);
        doTransaction(managedEntity, changeEntity);
    }

    public <T> List<T> executeQuery(String statement, Map<String, String> arguments, int maxResults){
        Query query = em.createQuery(statement);
        for(String key : arguments.keySet()){
            query.setParameter(key, arguments.get(key));
        }
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    public <T> T getOne(T entity, int id){
        return (T) em.find(entity.getClass(), id);
    }

    @Override
    public void close() throws Exception {
        generalRepositores.remove(this);
        em.close();
        emf.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repository that = (Repository) o;
        return ((Repository) o).getDataBaseName() == dataBaseName;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "repositoryType=" + repositoryType +
                ", dataBaseName='" + dataBaseName + '\'' +
                '}';
    }

    public String getDataBaseName() {
        return dataBaseName;
    }
}
