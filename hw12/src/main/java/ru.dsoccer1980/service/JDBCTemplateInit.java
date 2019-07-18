package ru.dsoccer1980.service;

import org.hibernate.SessionFactory;
import ru.dsoccer1980.cache.CacheEngine;
import ru.dsoccer1980.cache.CacheEngineImpl;
import ru.dsoccer1980.dao.HibernateImpl;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.User;

import java.sql.SQLException;

public class JDBCTemplateInit {

    public JdbcTemplate<User> init(SessionFactory sessionFactory) throws SQLException {
        int size = 5;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(size, 1000, 0, false);

        JdbcTemplate<User> userTemplate = new HibernateImpl<>(sessionFactory, cache);
        User user1 = new User("User1 name", 23);
        User user2 = new User("User2 name", 39);
        userTemplate.create(user1);
        userTemplate.create(user2);

        return userTemplate;
    }


}
