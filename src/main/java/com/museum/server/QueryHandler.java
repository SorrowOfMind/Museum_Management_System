package com.museum.server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
;
import java.util.List;


public class QueryHandler<T> {
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet result;

    public List<T> ExecuteDatabaseQuery(String query) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("YourPersistenceUnitName");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        List<T> objects = null;

        try {
            entityManager.getTransaction().begin();

            // HQL (Hibernate Query Language) to select all rows from the table
            Query preparedQuery = entityManager.createQuery(query);
            objects = preparedQuery.getResultList();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return objects;
    }

}
