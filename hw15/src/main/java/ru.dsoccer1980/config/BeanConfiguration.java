package ru.dsoccer1980.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.dsoccer1980.cache.CacheEngine;
import ru.dsoccer1980.cache.CacheEngineImpl;
import ru.dsoccer1980.dao.HibernateImpl;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.service.HibernateUtils;

@Configuration
public class BeanConfiguration {

    @Bean
    public JdbcTemplate<User> getJdbcTemplate(CacheEngine<Long, User> cache, SessionFactory sessionFactory) {
        return new HibernateImpl<>(sessionFactory, cache);
    }

    @Bean
    public CacheEngine<Long, User> getCacheEngine() {
        return new CacheEngineImpl<>(5, 1000, 0, false);
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return HibernateUtils.getSessionFactory("hibernate.cfg.xml", User.class);
    }


}
