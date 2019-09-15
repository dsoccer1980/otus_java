package ru.dsoccer1980.service;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.SessionFactory;
import ru.dsoccer1980.cache.CacheEngine;
import ru.dsoccer1980.dao.HibernateImpl;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystem;


public class DBServiceMessageSystemClientImpl extends HibernateImpl<User> implements DBServiceMessageSystemClient {

    @Getter
    private final MessageSystem messageSystem;
    @Getter
    @Setter
    private Address address;

    public DBServiceMessageSystemClientImpl(SessionFactory sessionFactory, CacheEngine<Long, User> cacheEngine, MessageSystem messageSystem) {
        super(sessionFactory, cacheEngine);
        this.messageSystem = messageSystem;
    }

}
