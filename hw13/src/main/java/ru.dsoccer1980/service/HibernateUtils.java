package ru.dsoccer1980.service;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    public static SessionFactory getSessionFactory(String configFile, Class<?>... classes) {
        SessionFactory sessionFactory;
        Configuration configuration = new Configuration()
                .configure(configFile);

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        for (Class<?> clazz : classes) {
            metadataSources.addAnnotatedClass(clazz);
        }

        Metadata metadata = metadataSources.getMetadataBuilder().build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
        return sessionFactory;
    }
}
