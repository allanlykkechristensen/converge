/*
 * Copyright (C) 2009 - 2013 Converge Consulting Ltd
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
package com.getconverge.converge.ejb.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Stateless session bean providing a data access object service for accessing
 * entities.
 *
 * @author Allan Lykke Christensen
 */
@Stateless
@LocalBean
public class DaoServiceBean {

    private static final String QUERY_FIND_ALL_BY_TYPE = "SELECT o FROM %1$s AS o";
    private static final String QUERY_FIND_ALL_BY_TYPE_ORDERED_BY_FIELD_IN_DIRECTION = "SELECT o FROM %1$s AS o ORDER BY o.%2$s %3$s";
    private static final String QUERY_COUNT_ROWS_BY_TYPE = "SELECT COUNT(o.%1$s) from %2$s o";
    private static final String QUERY_SORTING_ASCENDING = "ASC";
    private static final String QUERY_SORTING_DESCENDING = "DESC";

    @PersistenceContext
    private EntityManager em;

    /**
     * Stores a given object in the data store.
     *
     * @param <T> Type of entity to store
     * @param t Entity to store
     * @return Object stored in the data store.
     */
    public <T> T create(T t) {
        this.em.persist(t);
        this.em.flush();
        this.em.refresh(t);
        return t;
    }

    /**
     * Finds a given entity in the data store.
     *
     * @param <T> Type of entity
     * @param type Type of entity
     * @param id Unique identifier of the entity
     * @return Entity matching the unique identifier
     * @throws DataNotFoundException If no match could be found
     */
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
     * @param type Type of object
     * @param id Unique identifier of the object
     */
    public void delete(Class type, Object id) {
        if (id == null) {
            return;
        }
        Object objectToDelete = this.em.find(type, id);
        if (objectToDelete != null) {
            this.em.remove(objectToDelete);
        }
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param <T> Type of entity
     * @param t Entity to update
     * @return Updated entity
     * @throws OptimisticLockException If {@code t} is outdated, and updated
     * prior to the call of this method
     */
    public <T> T update(T t) {
        return this.em.merge(t);
    }

    /**
     * Executes a query on the database and returns the number of records
     * affected.
     *
     * @param namedQueryName Name of the Named Query
     * @param qb QueryBuilder containing the parameters
     * @return Number of affected records
     */
    public int executeNamedQuery(String namedQueryName, QueryBuilder qb) {
        Set<Entry<String, Object>> rawParameters = qb.parameters().entrySet();
        Query query = this.em.createNamedQuery(namedQueryName);

        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.executeUpdate();
    }

    public int executeQuery(String query) {
        return this.em.createQuery(query).executeUpdate();
    }

    /**
     * Executes a query on the database and returns the number of records
     * affected.
     *
     * @param namedQueryName Name of the Named Query
     * @return Number of affected records
     */
    public int executeNamedQuery(String namedQueryName) {
        Query query = this.em.createNamedQuery(namedQueryName);
        return query.executeUpdate();
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param namedQueryName Name of the query
     * @return {@link List} of entities returned by the given query
     */
    public List findWithNamedQuery(String namedQueryName) {
        return this.em.createNamedQuery(namedQueryName).getResultList();
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param namedQueryName Name of the query
     * @param parameters Parameters of the query
     * @return {@link List} of entities returned by the given query
     */
    public List findWithNamedQuery(String namedQueryName, Map<String, Object> parameters) {
        return findWithNamedQuery(namedQueryName, parameters, 0);
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param <T> Type of object to retrieve
     * @param type Type of object to retrieve
     * @param namedQueryName Name of the query
     * @param parameters Parameters of the query
     * @return Matched entity
     * @throws DataNotFoundException If an entity could not be found
     */
    public <T> T findObjectWithNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters) throws DataNotFoundException {
        @SuppressWarnings("unchecked")
        List<T> results = findWithNamedQuery(namedQueryName, parameters, 0);

        if (results.isEmpty()) {
            throw new DataNotFoundException("Not found");
        } else {
            return results.iterator().next();
        }
    }

    /**
     * Execute a {@link NamedQuery} and return a single result of the given
     * type.
     *
     * @param <T> Type of entity
     * @param type Type to retrieve
     * @param namedQueryName Name of the {@link NamedQuery}
     * @param parameters Parameters to use in the {@link NamedQuery}
     * @return Single result of type {@code T} from the given {@link NamedQuery}
     */
    public <T> T executeNamedQuery(Class<T> type, String namedQueryName, Map<String, Object> parameters) {
        Set<Entry<String, Object>> rawParameters = parameters.entrySet();
        Query query = this.em.createNamedQuery(namedQueryName);

        for (Entry<String, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return (T) query.getSingleResult();
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param <T> Type of object to retrieve
     * @param type Type of object to retrieve
     * @param namedQueryName Name of the query
     * @param queryBuilder Builder containing the query parameters
     * @return Matched entity
     * @throws DataNotFoundException If an entity could not be found
     */
    public <T> T findObjectWithNamedQuery(Class<T> type, String namedQueryName, QueryBuilder queryBuilder) throws DataNotFoundException {
        return findObjectWithNamedQuery(type, namedQueryName, queryBuilder.parameters());
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param queryName Name of the query
     * @param resultLimit Maximum number of results
     * @return {@link List} of entities returned by the given query
     */
    public List findWithNamedQuery(String queryName, int resultLimit) {
        return this.em.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    /**
     * Finds a {@link List} of entity returned by the given named query.
     *
     * @param namedQueryName Name of the query
     * @param parameters Parameters of the query
     * @param resultLimit Maximum number of results
     * @return {@link List} of entities returned by the given query
     */
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
     * @param parameters Parameters of the query
     * @param start First record of the result set
     * @param resultLimit Maximum number of results
     * @return {@link List} of entities returned by the given query
     */
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
     * Finds a {@link List} of entity returned by the given {@link Query}.
     *
     * @param query {@link Query} to execute
     * @param parameters Parameters of the query
     * @param start First record of the result set
     * @param resultLimit Maximum number of results
     * @return {@link List} of entities returned by the {@link Query}
     */
    public List findWithQuery(Query query, Map<String, Object> parameters, int start, int resultLimit) {
        Set<Entry<String, Object>> rawParameters = parameters.entrySet();

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
     * Finds a {@link List} of entities returned from the given native SQL
     * query.
     *
     * @param sql Native SQL query
     * @param type Type of entity
     * @return {@link List} of entities returned from the given native SQL query
     */
    public List findByNativeQuery(String sql, Class type) {
        return this.em.createNativeQuery(sql, type).getResultList();
    }

    /**
     * Finds all the entities of a given type.
     *
     * @param <T> Type of entity
     * @param type Type of entity
     * @return {@link List} of all entities of the given type
     */
    public <T> List<T> findAll(Class<T> type) {
        String query = String.format(QUERY_FIND_ALL_BY_TYPE, type.getSimpleName());
        return new LinkedList(this.em.createQuery(query).getResultList());
    }

    /**
     * Finds all the entities of a given type sorted by a given field in a given
     * direction.
     *
     * @param <T> Type of entity
     * @param type Type of entity
     * @param orderBy Field to sort by
     * @param asc Sorting direction
     * @return {@link List} of all entities of the given type sorted by the
     * given field in the given direction
     */
    public <T> List<T> findAll(Class<T> type, String orderBy, boolean asc) {
        String query = String.format(QUERY_FIND_ALL_BY_TYPE_ORDERED_BY_FIELD_IN_DIRECTION, type.getSimpleName(), orderBy, getSortingDirection(asc));
        return new LinkedList(this.em.createQuery(query).getResultList());
    }

    /**
     * Finds a range of entities of a given type in a given range.
     *
     * @param <T> Type of entity
     * @param type Type of entity
     * @param start First entity to retrieve
     * @param resultLimit Number of entities to retrieve
     * @return {@link List} of entities of the given type in the given range
     */
    public <T> List<T> findAll(Class<T> type, int start, int resultLimit) {
        String query = String.format(QUERY_FIND_ALL_BY_TYPE, type.getSimpleName());
        return new LinkedList(this.em.createQuery(query).setFirstResult(start).setMaxResults(resultLimit).getResultList());
    }

    /**
     * Finds a range of entities of a given type in a given range sorted by a
     * given field in a given direction.
     *
     * @param <T> Type of entity
     * @param type Type of entity
     * @param start First entity to retrieve
     * @param resultLimit Number of entities to retrieve
     * @param orderBy Field to sort by
     * @param asc Sorting direction
     * @return {@link List} of entities of the given type in the given range
     * sorted by the given field in the given direction
     */
    public <T> List<T> findAll(Class<T> type, int start, int resultLimit, String orderBy, boolean asc) {
        String query = String.format(QUERY_FIND_ALL_BY_TYPE_ORDERED_BY_FIELD_IN_DIRECTION, type.getSimpleName(), orderBy, getSortingDirection(asc));
        return new LinkedList(this.em.createQuery(query).setFirstResult(start).setMaxResults(resultLimit).getResultList());
    }

    /**
     * Count the number of entities of a given type.
     *
     * @param <T> Type of entity
     * @param type Type of entity
     * @param field Field to count in the entity
     * @return Number of entities of the given type
     */
    public <T> Number count(Class<T> type, String field) {
        String query = String.format(QUERY_COUNT_ROWS_BY_TYPE, field, type.getSimpleName());
        return (Number) this.em.createQuery(query).getSingleResult();
    }

    /**
     * Obtain the {@link EntityManager} from the persistence framework.
     *
     * <em>Developer note: It is questionable if the entity manager should be
     * exposed</em>.
     *
     * @return {@link EntityManager} from the persistence framework
     */
    public EntityManager getEntityManager() {
        return em;
    }

    /**
     * Utility function for turning a boolean value into a value query sorting
     * direction.
     *
     * @param ascending Direction, where {@code true} is ascending and
     * {@code false} is descending
     * @return {@link #QUERY_SORTING_ASCENDING} if {@code ascending} is
     * {@code true}, otherwise {@link #QUERY_SORTING_DESCENDING}
     */
    private String getSortingDirection(boolean ascending) {
        return ascending ? QUERY_SORTING_ASCENDING : QUERY_SORTING_DESCENDING;
    }
}
