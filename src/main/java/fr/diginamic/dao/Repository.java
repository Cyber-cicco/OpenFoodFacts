package fr.diginamic.dao;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Repository implements AutoCloseable{

    private final EntityManager em;
    private final EntityManagerFactory emf;
    private final String SELECT = "select e from %s e";
    private final String SELECT_WITH_CONDITION = "select e from %s e where %s = %s";
    private static List<Repository> generalRepositores = new ArrayList<>();

    private final String dataBaseName;
    private Repository(String dataBaseName){
        emf = Persistence.createEntityManagerFactory(dataBaseName);
        em = emf.createEntityManager();
        this.dataBaseName = dataBaseName;
    }

    public static Repository getInstance(String dataBaseName){
        List<Repository> repository = generalRepositores.stream().filter(generalRepository -> generalRepository.getDataBaseName()  == dataBaseName).toList();
        if(repository.size() > 0){
            return repository.get(0);
        }
        return new Repository(dataBaseName);
    }

    public <T> void persistEntity(T entity){
        doTransaction(entity, em::persist);
    }

    public <T> void doTransaction(T entity, Consumer<T> transaction){
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        transaction.accept(entity);
        tr.commit();
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

    public String getDataBaseName() {
        return dataBaseName;
    }
}
