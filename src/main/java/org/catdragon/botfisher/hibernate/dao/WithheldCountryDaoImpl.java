package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.pojo.WithheldCountry;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class WithheldCountryDaoImpl
        extends GenericHibernateDao<WithheldCountry, Integer>
        implements WithheldCountryDao {

    @Override
    public WithheldCountry create(WithheldCountry entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        create(entity, session);
        commit(t, session);
        return entity;
    }

    private WithheldCountry create(WithheldCountry entity, Session session) {
        session.save(entity);
        return entity;
    }

    public WithheldCountry create(String countryCode) {
        Session session = getSession();
        Transaction t = session.beginTransaction();

        WithheldCountry withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode(countryCode);

        create(withheldCountry, session);
        commit(t, session);
        return withheldCountry;
    }

    public WithheldCountry read(String countryCode) {
        Session session = getSession();
        Transaction t = session.beginTransaction();

        WithheldCountry withheldCountry = read(countryCode, session);

        commit(t, session);
        return withheldCountry;
    }

    private WithheldCountry read(String countryCode, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<WithheldCountry> query = builder.createQuery(WithheldCountry.class);
        Root<WithheldCountry> root = query.from(WithheldCountry.class);
        query.select( root );
        query.where( builder.equal( root.<String>get(COUNTRY_CODE_FIELD), countryCode ) );
        WithheldCountry withheldCountry = getSession().createQuery( query ).uniqueResult();
        return withheldCountry;
    }

    @Override
    public WithheldCountry read(Integer integer) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        WithheldCountry withheldCountry = read(integer, session);

        commit(t, session);
        return withheldCountry;
    }

    WithheldCountry read(Integer integer, Session session) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<WithheldCountry> query = builder.createQuery(WithheldCountry.class);
        Root<WithheldCountry> root = query.from(WithheldCountry.class);
        query.select( root );
        query.where( builder.equal( root.<String>get(ID_FIELD), integer ) );

        WithheldCountry withheldCountry = session.createQuery( query ).uniqueResult();
        return withheldCountry;
    }

    /**
     * need a lock? don't really think so in single thread
     * @param integer
     * @param lock
     * @return
     */
    @Override
    public WithheldCountry read(Integer integer, boolean lock) {
        return read(integer);
    }

    @Override
    public List<WithheldCountry> readAll() {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        List<WithheldCountry> withheldCountries = readAll(session);

        commit(t, session);
        return withheldCountries;
    }

    private List<WithheldCountry> readAll(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<WithheldCountry> query = builder.createQuery(WithheldCountry.class);
        Root<WithheldCountry> root = query.from(WithheldCountry.class);
        query.select( root );
        List<WithheldCountry> withheldCountries = session.createQuery( query ).getResultList();
        return withheldCountries;
    }

    @Override
    public WithheldCountry update(WithheldCountry entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        entity = update(entity, session);
        commit(t, session);
        return entity;
    }

    private WithheldCountry update(WithheldCountry entity, Session session) {
        session.update(entity);
        return entity;
    }

    @Override
    public WithheldCountry createOrUpdate(WithheldCountry entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        entity = createOrUpdate(entity, session);
        commit(t, session);
        return entity;
    }

    private WithheldCountry createOrUpdate(WithheldCountry entity, Session session) {
        WithheldCountry withheldCountry = read(entity.getCountryCode(), session);
        if (withheldCountry != null) {
            return withheldCountry;
        }
        create(entity, session);
        return entity;
    }

    @Override
    public WithheldCountry createOrUpdate(String countryCode) {
        WithheldCountry withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode(countryCode);
        return createOrUpdate(withheldCountry);
    }

    @Override
    public void delete(WithheldCountry entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        delete(entity, session);
        commit(t, session);
    }

    @Override
    public void delete(String countryCode) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        WithheldCountry withheldCountry = read(countryCode, session);
        if (withheldCountry != null) {
            delete(withheldCountry, session);
        }
        commit(t, session);
    }

    private void delete(WithheldCountry entity, Session session) {
        session.delete(entity);
    }
   }