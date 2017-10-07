package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.pojo.Follower;
import org.catdragon.botfisher.hibernate.pojo.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class FollowerDaoImpl extends GenericHibernateDao<Follower, Long>
        implements FollowerDao {

    UserDao userDao;

    public FollowerDaoImpl() {
        DaoFactory factory = DaoFactory.instance(DaoFactory.HIBERNATE);
        userDao = factory.getUserDao();
    }

    @Override
    public Follower create(Follower entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        create(entity, session);
        commit(t, session);
        return entity;
    }

    private Follower create(Follower f, Session session) {
        updateUpdated(f);
        session.save(f);
        return f;
    }

    @Override
    public Follower read(Long id) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        Follower f = read(id, session);

        commit(t, session);
        return f;
    }

    public Follower read(Long id, Session session) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Follower> query = builder.createQuery(Follower.class);
        Root<Follower> root = query.from(Follower.class);
        query.select(root);
        query.where(builder.equal(root.<String>get(ID_FIELD), id));

        Follower f = session.createQuery(query).uniqueResult();
        return f;
    }

    @Override
    public Follower read(String followeeScreenName, String followerScreenName) {
        User followee = userDao.read(followeeScreenName);
        User follower = userDao.read(followerScreenName);
        return read(followee, follower);
    }

    @Override
    public Follower read(User followee, User follower) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        Follower f = read(followee, follower, session);
        commit(t, session);
        return f;
    }

    private Follower read(User followee, User follower, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Follower> query = builder.createQuery(Follower.class);
        Root<Follower> root = query.from(Follower.class);
        query.select(root);
        query.where(
                builder.and(
                        builder.equal(root.<String>get(FOLLOWEE_FIELD),
                                followee.getId()),
                        builder.equal(root.<String>get(FOLLOWER_FIELD),
                                follower.getId()))
        );
        Follower user = session.createQuery(query).uniqueResult();
        return user;
    }

    @Override
    public Follower read(Long id, boolean lock) {
        return read(id);
    }

    @Override
    public List<Follower> readAll() {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        List<Follower> users = readAll(session);
        commit(t, session);
        return users;
    }

    private List<Follower> readAll(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Follower> query = builder.createQuery(Follower.class);
        Root<Follower> root = query.from(Follower.class);
        query.select(root);
        List<Follower> users = session.createQuery(query).getResultList();
        return users;
    }

    @Override
    public Follower update(Follower entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        entity = update(entity, session);
        commit(t, session);
        return entity;
    }

    private Follower update(Follower entity, Session session) {
        updateUpdated(entity);
        session.update(entity);
        return entity;
    }

    @Override
    public Follower activate(Follower entity) {
        entity.setActive(true);
        return update(entity);
    }

    @Override
    public Follower deactivate(Follower entity) {
        entity.setActive(false);
        return update(entity);
    }

    @Override
    public Follower createOrUpdate(Follower entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        entity = createOrUpdate(entity, session);
        commit(t, session);
        return entity;
    }

    private Follower createOrUpdate(Follower entity, Session session) {
        updateUpdated(entity);
        session.saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void delete(Follower entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        delete(entity, session);
        commit(t, session);
    }

    @Override
    public void delete(String followeeScreenName, String followerScreenName) {
        Follower f = read(followeeScreenName, followerScreenName);
        Session session = getSession();
        Transaction t = session.beginTransaction();
        if (f != null) {
            delete(f, session);
        }
        commit(t, session);
    }

    private void delete(Follower entity, Session session) {
        session.delete(entity);
    }

    public void updateUpdated(Follower f) {
        f.setUpdatedAt(new Date());
    }

}