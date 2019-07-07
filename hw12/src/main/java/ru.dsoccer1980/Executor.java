package ru.dsoccer1980;

import org.hibernate.SessionFactory;
import ru.dsoccer1980.cache.CacheEngine;
import ru.dsoccer1980.cache.CacheEngineImpl;
import ru.dsoccer1980.dao.HibernateImpl;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.Account;
import ru.dsoccer1980.domain.AddressDataSet;
import ru.dsoccer1980.domain.PhoneDataSet;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.server.ServerImpl;
import ru.dsoccer1980.service.HibernateUtils;

public class Executor {
    private final static int PORT = 8080;

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory("hibernate.cfg.xml",
                Account.class, User.class, AddressDataSet.class, PhoneDataSet.class);

        int size = 5;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(size, 1000, 0, false);

        JdbcTemplate<User> userTemplate = new HibernateImpl<>(sessionFactory, cache);
        User user1 = new User("User1 name", 23);
        User user2 = new User("User2 name", 39);
        userTemplate.create(user1);
        userTemplate.create(user2);


        new ServerImpl(userTemplate, PORT).start();
    }


}

