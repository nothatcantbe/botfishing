package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.pojo.WithheldCountry;
import org.catdragon.botfisher.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateDaoFactory extends DaoFactory {

    private GenericHibernateDao instantiateDAO(Class daoClass) {
        try {
            GenericHibernateDao dao = (GenericHibernateDao)daoClass.newInstance();
            dao.setSession(getCurrentSession());
            return dao;
        } catch (Exception ex) {
            throw new RuntimeException("Can not instantiate DAO: " + daoClass, ex);
        }
    }

    // You could override this if you don't want HibernateUtil for lookup
    protected Session getCurrentSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }

    @Override
    public UserDao getUserDao() {
        return (UserDao) instantiateDAO(UserDaoImpl.class);
    }

    @Override
    public WithheldCountryDao getWithheldCountryDao() {
        return (WithheldCountryDao) instantiateDAO(WithheldCountryDaoImpl.class);
    }

    @Override
    public FollowerDao getFollowerDao() {
        return (FollowerDao) instantiateDAO(FollowerDaoImpl.class);
    }
}
