package org.catdragon.botfisher.hibernate.util;

import org.apache.log4j.Logger;
import org.catdragon.botfisher.hibernate.pojo.Follower;
import org.catdragon.botfisher.hibernate.pojo.User;
import org.catdragon.botfisher.hibernate.pojo.WithheldCountry;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static Logger logger = Logger.getLogger(HibernateUtil.class);

    private static final SessionFactory sessionFactory = buildSessionFactory();
    public static Configuration configuration;
    public static MetadataSources metadataSources;

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(WithheldCountry.class);
            configuration.addAnnotatedClass(Follower.class);
            logger.info("Hibernate Configuration loaded");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            logger.info("Hibernate serviceRegistry created");

            metadataSources = new MetadataSources(serviceRegistry);

            SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            logger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
