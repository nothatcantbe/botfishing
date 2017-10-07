package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.omg.IOP.TransactionService;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class GenericHibernateDao<T, ID extends Serializable>
        implements GenericDao<T, ID> {

    private Class<T> persistentClass;
    private Session session;

    public GenericHibernateDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setSession(Session session) {
        this.session = session;
    }

    protected Session getSession() {
        if (session == null)
            session = HibernateUtil.getSessionFactory().openSession();
        if (!session.isOpen()) {
            session = HibernateUtil.getSessionFactory().openSession();
        }

        return session;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public void commit(Transaction t, Session s) {
        t.commit();
        s.close();
    }
}


