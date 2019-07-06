package ru.dsoccer1980.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.dsoccer1980.cache.CacheEngine;
import ru.dsoccer1980.cache.MyElement;

public class HibernateImpl<T> implements JdbcTemplate<T> {
    private final SessionFactory sessionFactory;
    private CacheEngine<Long, T> cacheEngine;

    public HibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public HibernateImpl(SessionFactory sessionFactory, CacheEngine<Long, T> cacheEngine) {
        this.sessionFactory = sessionFactory;
        this.cacheEngine = cacheEngine;
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
        if (cacheEngine != null) {
            MyElement<Long, T> myElement = cacheEngine.get(id);
            if (myElement != null) {
                return myElement.getValue();
            }
        }

        try (Session session = sessionFactory.openSession()) {
            T t = session.get(clazz, id);
            if (cacheEngine != null) {
                cacheEngine.put(new MyElement<>(id, t));
            }
            return t;
        }
    }

}
