package ru.dsoccer1980.jdbc;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateImpl<T> implements JdbcTemplate<T> {
    private static final String URL = "jdbc:h2:mem:testDB;DB_CLOSE_DELAY=-1";
    private final SessionFactory sessionFactory;

    public HibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(T objectData) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(objectData);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(T objectData) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(objectData);
            session.getTransaction().commit();
        }

    }

    @Override
    public T load(long id, Class<T> clazz) {
        try (Session session = sessionFactory.openSession()) {
            T t = session.get(clazz, id);
            return t;
        }
    }
}
