package ru.dsoccer1980.service;


import lombok.Getter;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import ru.dsoccer1980.cache.CacheEngine;
import ru.dsoccer1980.dao.HibernateImpl;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystem;


@Service
public class DBServiceMessageSystemClientImpl extends HibernateImpl<User> implements DBServiceMessageSystemClient {

    @Getter
    private final Address address = new Address("DBService");

    @Getter
    private final MessageSystem messageSystem;

    public DBServiceMessageSystemClientImpl(SessionFactory sessionFactory, CacheEngine<Long, User> cacheEngine, MessageSystem messageSystem) {
        super(sessionFactory, cacheEngine);
        this.messageSystem = messageSystem;
    }

}
