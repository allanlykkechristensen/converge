/*
 * Copyright (C) 2009 - 2012 Interactive Media Management
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.i2m.converge.ejb.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.*;

/**
 * Stateless session bean providing a data access object service for accessing
 * entities.
 *
 * @author Allan Lykke Christensen
 */
@Stateless
public class DaoServiceBean implements DaoServiceLocal {

    @PersistenceContext(unitName = "convergePlatformPU")
    private EntityManager em;

    /**
     * Stores a given object in the data store.
     *
     * @param <T>
     *          Type of entity to store
     * @param t
     *          Entity to store
     * @return Object stored in the data store.
     */
    @Override
    public <T> T create(T t) {
        this.em.persist(t);
        this.em.flush();
        this.em.refresh(t);
        return t;
    }

    @Override
    public void commit() {
        this.em.getTransaction().commit();
    }

    /**
     * Finds a given entity in the data store.
     *
     * @param <T>
     *          Type of entity
     * @param type
     *          Type of entity
     * @param id
     *          Unique identifier of the entity
     * @return Entity matching the unique identifier
     * @throws DataNotFoundException
     *          If no match could be found
     */
    @Override
    public <T> T findById(Class<T> type, Object id) throws DataNotFoundException {
        if (id == null) {
            throw new DataNotFoundException("null is not a valid primary key for " + type.getName());
        }

        T entity = this.em.find(type, id);

        if (entity == null) {
            throw new DataNotFoundException(type.getName() + " with ID " + id + " not found");
        }
        return entity;
    }

    /**
     * Remove a given object from the data store.
     *
     * @param type
     *          Type of object
     * @param id
     *          Unique identifier of the object
     */
    @Override
    public void delete(Class type, Object id) {
        if (id == null) {
            return;
        }
        //Object ref = this.em.getReference(type, id);
        Object obj = this.em.find(type, id);
        if (obj != null) {
            this.em.remove(obj);
        }
    }

    /** {@inheritDoc } */
    @Override
    public <T> T update(T t) throws OptimisticLockException {
        return this.em.merge(t);
    }

    /** {@inheritDoc } */
    @Override
    public int executeQuery(String namedQueryName, QueryBuilder qb) {
        Set<Entry<String, Object>> rawParameters = qb.parameters().entrySet();
        Query query = this.em.createNamedQuery(namedQueryName);

        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.executeUpdate();
    }

    /** {@inheritDoc } */
    @Override
    public int executeQuery(String namedQueryName) {
        Query query = this.em.createNamedQuery(namedQueryName);
        return query.executeUpdate();
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param namedQueryName
     *          Name of the query
     * @return {@link List} of entities returned by the given query
     */
    @Override
    public List findWithNamedQuery(String namedQueryName) {
        return this.em.createNamedQuery(namedQueryName).getResultList();
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param namedQueryName
     *          Name of the query
     * @param parameters
     *          Parameters of the query
     * @return {@link List} of entities returned by the given query
     */
    @Override
    public List findWithNamedQuery(String namedQueryName, Map<String, Object> parameters) {
        return findWithNamedQuery(namedQueryName, parameters, 0);
    }

    /** {@inheritDoc } */
    @Override
    public <T> T findObjectWithNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters) throws DataNotFoundException {
        @SuppressWarnings("unchecked")
        List<T> results = findWithNamedQuery(namedQueryName, parameters, 0);

        if (results.isEmpty()) {
            throw new DataNotFoundException("Not found");
        } else {
            return results.iterator().next();
        }
    }

    /** {@inheritDoc } */
    @Override
    public <T> T findObjectWithNamedQuery(Class<T> type, String namedQueryName, QueryBuilder queryBuilder) throws DataNotFoundException {
        return findObjectWithNamedQuery(type, namedQueryName, queryBuilder.parameters());
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param queryName
     *          Name of the query
     * @param resultLimit
     *          Maximum number of results
     * @return {@link List} of entities returned by the given query
     */
    @Override
    public List findWithNamedQuery(String queryName, int resultLimit) {
        return this.em.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param namedQueryName
     *          Name of the query
     * @param parameters
     *          Parameters of the query
     * @param resultLimit
     *          Maximum number of results
     * @return {@link List} of entities returned by the given query
     */
    @Override
    public List findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int resultLimit) {
        Set<Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = this.em.createNamedQuery(namedQueryName);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param namedQueryName Name of the query
     * @param parameters     Parameters of the query
     * @param start          First record of the result set
     * @param resultLimit    Maximum number of results
     * @return {@link List} of entities returned by the given query
     */
    @Override
    public List findWithNamedQuery(String namedQueryName, Map<String, Object> parameters, int start, int resultLimit) {
        Set<Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = this.em.createNamedQuery(namedQueryName);
        if (start > -1) {
            query.setFirstResult(start);
        }
        query.setFirstResult(start);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }


        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    /**
     * Retrieve a paged {@link DaoResult} based on a named query. 
     *
     * @param namedQuery 
     *          Name of the query
     * @param parameters     
     *          Parameters of the query
     * @param page          
     *          Page to retrieve (not zero-based)
     * @param resultsPerPage    
     *          Number of results per page
     * @return {@link List} of entities returned by the given query
     */
    @Override
    public <T> DaoResult<T> findDaoResultWithNamedQuery(Class<T> type, String namedQuery, QueryBuilder builder, int page, int resultsPerPage) {

        // Start timing the query
        long time = System.currentTimeMillis();
        
        // Obtain the named query
        TypedQuery<T> query = this.em.createNamedQuery(namedQuery, type);
        
        // Extract the parameters from the QueryBuilder
        Map<String, Object> p = builder.parameters();
        //Set<Entry<String, Object>> rawParameters = p.entrySet();
        for (Entry<String, Object> entry : p.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // Generate a Paging List
        PagingList<T> results = new PagingList<T>(query, resultsPerPage);
        
        // Copy the results from the Paging List into the DaoResult
        DaoResult<T> result = new DaoResult<T>();
        result.setResults(results.getPage(page));
        result.setNumberOfResults(results.size());
        result.setResultsPerPage(resultsPerPage);
        result.setPage(page);
        
        // Stop timing
        time = time - System.currentTimeMillis();
        result.setSearchTime(time);

        return result;
    }

    /**
     * Finds a {@link List} of entities returned from the given native SQL
     * query.
     *
     * @param sql
     *          Native SQL query
     * @param type
     *          Type of entity
     * @return {@link List} of entities returned from the given native SQL query
     */
    @Override
    public List findByNativeQuery(String sql, Class type) {
        return this.em.createNativeQuery(sql, type).getResultList();
    }

    /**
     * Finds all the entities of a given type.
     *
     * @param <T>
     *          Type of entity
     * @param type
     *          Type of entity
     * @return {@link List} of all entities of the given type
     */
    @Override
    public <T> List<T> findAll(Class<T> type) {
        return new LinkedList(this.em.createQuery("SELECT o from " + type.getSimpleName() + " AS o").getResultList());
    }

    @Override
    public <T> List<T> findAll(Class<T> type, String orderBy, boolean asc) {
        String direction = asc ? "ASC" : "DESC";
        return new LinkedList(this.em.createQuery("SELECT o from " + type.getSimpleName() + " AS o ORDER BY o." + orderBy + " " + direction).getResultList());
    }

    @Override
    public <T> List<T> findAll(Class<T> type, int start, int resultLimit) {
        return new LinkedList(this.em.createQuery("SELECT o from " + type.getSimpleName() + " AS o").setFirstResult(start).setMaxResults(resultLimit).getResultList());
    }

    @Override
    public <T> List<T> findAll(Class<T> type, int start, int resultLimit, String orderBy, boolean asc) {
        String direction = asc ? "ASC" : "DESC";
        return new LinkedList(this.em.createQuery("SELECT o from " + type.getSimpleName() + " AS o ORDER BY o." + orderBy + " " + direction).setFirstResult(start).setMaxResults(resultLimit).getResultList());
    }

    @Override
    public <T> Number count(Class<T> type, String field) {
        return (Number) this.em.createQuery("SELECT COUNT(o." + field + ") from " + type.getSimpleName() + " o").getSingleResult();
    }
}
