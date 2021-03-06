package ru.dsoccer1980.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.dsoccer1980.cache.CacheEngine;
import ru.dsoccer1980.cache.MyElement;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

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
            updateObjectInCache(objectData);
        }

    }

    @Override
    public T load(long id, Class<T> clazz) {
        if (getFromCache(id).isPresent()) {
            return getFromCache(id).get();
        }

        try (Session session = sessionFactory.openSession()) {
            T t = session.get(clazz, id);
            putInCache(id, t);
            return t;
        }
    }

    @Override
    public List<T> getAll(Class<T> clazz) {
        try (Session session = sessionFactory.openSession()) {
            Query<T> query = session.createQuery("select u from User u", clazz);
            return query.getResultList();
        }
    }

    private Optional<T> getFromCache(long id) {
        Optional<T> result = Optional.empty();
        if (cacheEngine != null) {
            Optional<MyElement<Long, T>> myElement = cacheEngine.get(id);
            result = myElement.map(MyElement::getValue);
        }
        return result;
    }

    private void putInCache(long id, T t) {
        if (cacheEngine != null) {
            cacheEngine.put(new MyElement<>(id, t));
        }
    }

    private void updateObjectInCache(T objectData) {
        if (cacheEngine != null) {
            getId(objectData).ifPresent(id -> new MyElement<>(id, objectData));
        }
    }

    private Optional<Long> getId(T objectData) {
        try {
            Field fieldId = objectData.getClass().getDeclaredField("id");
            fieldId.setAccessible(true);
            Long id = (Long) fieldId.get(objectData);
            return Optional.ofNullable(id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
