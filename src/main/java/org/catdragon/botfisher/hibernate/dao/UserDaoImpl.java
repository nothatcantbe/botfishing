package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.pojo.User;
import org.catdragon.botfisher.hibernate.pojo.WithheldCountry;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDaoImpl extends GenericHibernateDao<User, Long> implements UserDao {

    WithheldCountryDao withheldCountryDao;

    public UserDaoImpl() {
        DaoFactory factory = DaoFactory.instance(DaoFactory.HIBERNATE);
        withheldCountryDao = factory.getWithheldCountryDao();
    }

    @Override
    public User create(User entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        create(entity, session);
        commit(t, session);
        return entity;
    }

    @Override
    public User create(twitter4j.User twitterUser) {
        User user = fromTwitterUser(twitterUser);
        return create(user);
    }

    private User create(User user, Session session) {
        updateUpdated(user);
        session.save(user);
        return user;
    }

    @Override
    public User read(Long id) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        User user = read(id, session);

        commit(t, session);
        return user;
    }

    @Override
    public User readTwitterId(Long id) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        User user = readTwitterId(id, session);

        commit(t, session);
        return user;
    }

     public User read(Long id, Session session) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select( root );
        query.where( builder.equal( root.<String>get(ID_FIELD), id ) );

        User user = session.createQuery( query ).uniqueResult();
        return user;
    }

    public User readTwitterId(Long id, Session session) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select( root );
        query.where( builder.equal( root.<String>get(TWITTER_ID_FIELD), id ) );

        User user = session.createQuery( query ).uniqueResult();
        return user;
    }

    @Override
    public User read(String screenName) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        User user = read(screenName, session);
        commit(t, session);
        return user;
    }

    private User read(String screenName, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select( root );
        query.where( builder.equal( root.<String>get(TWITTER_SCREEN_NAME_FIELD), screenName ) );
        User user = getSession().createQuery( query ).uniqueResult();
        return user;
    }

    @Override
    public User read(Long id, boolean lock) {
        return read(id);
    }

    @Override
    public List<User> readAll() {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        List<User> users = readAll(session);
        commit(t, session);
        return users;
    }

     private List<User> readAll(Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select( root );
        List<User> users = session.createQuery( query ).getResultList();
        return users;
    }

    @Override
    public User update(User entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        entity = update(entity, session);
        commit(t, session);
        return entity;
    }

    private User update(User entity, Session session) {
        updateUpdated(entity);
        session.update(entity);
        return entity;
    }

    @Override
    public User createOrUpdate(User entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        entity = createOrUpdate(entity, session);
        commit(t, session);
        return entity;
    }

    @Override
    public User createOrUpdate(twitter4j.User twitterUser) {
        User user = fromTwitterUser(twitterUser);
        return createOrUpdate(user);
    }

    private User createOrUpdate(User entity, Session session) {
        updateUpdated(entity);
        session.saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void delete(User entity) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        delete(entity, session);
        commit(t, session);
    }

     @Override
     public void delete(String screenName) {
        Session session = getSession();
        Transaction t = session.beginTransaction();
        User user = read(screenName, session);
        if (user != null) {
            delete(user, session);
        }
        commit(t, session);
    }

    public void delete(User entity, Session session) {
        session.delete(entity);
    }

    public User fromTwitterUser(twitter4j.User twitterUser) {
        User user = new User();
        user.setTwitterScreenName(twitterUser.getScreenName());
        user.setTwitterId(twitterUser.getId());
        user.setTwitterContributersEnabled(twitterUser.isContributorsEnabled());
        user.setTwitterCreatedAt(twitterUser.getCreatedAt());
        user.setTwitterDefaultProfile(twitterUser.isDefaultProfile());
        user.setTwitterDefaultProfileImage(twitterUser.isDefaultProfileImage());
        user.setTwitterDescription(twitterUser.getDescription());
        user.setTwitterFavouritesCount(twitterUser.getFavouritesCount());
        user.setTwitterFollowersCount(twitterUser.getFollowersCount());
        user.setTwitterFriendsCount(twitterUser.getFriendsCount());
        user.setTwitterGeoEnabled(twitterUser.isGeoEnabled());
        user.setTwitterLang(twitterUser.getLang());
        user.setTwitterListedCount(twitterUser.getListedCount());
        user.setTwitterLocation(twitterUser.getLocation());
        user.setTwitterName(twitterUser.getName());
        user.setTwitterProfileBackgroundColor(twitterUser.getProfileBackgroundColor());
        user.setTwitterProfileBackgroundImageUrl(twitterUser.getProfileBackgroundImageURL());
        user.setTwitterProfileBackgroundImageUrlHttps(twitterUser.getProfileBackgroundImageUrlHttps());
        user.setTwitterProfileBackgroundTile(twitterUser.isProfileBackgroundTiled());
        user.setTwitterProfileImageUrl(twitterUser.getProfileImageURL());
        user.setTwitterProfileImageUrlHttps(twitterUser.getProfileImageURLHttps());
        user.setTwitterProfileLinkColor(twitterUser.getProfileLinkColor());
        user.setTwitterProfileSidebarBorderColor(twitterUser.getProfileSidebarBorderColor());
        user.setTwitterProfileSidebarFillColor(twitterUser.getProfileSidebarFillColor());
        user.setTwitterProfileTextColor(twitterUser.getProfileTextColor());
        user.setTwitterProfileUseBackgroundImage(twitterUser.isProfileUseBackgroundImage());
        user.setTwitterProtected(twitterUser.isProtected());
        user.setTwitterStatusesCount(twitterUser.getStatusesCount());
        user.setTwitterTimeZone(twitterUser.getTimeZone());
        user.setTwitterUrl(twitterUser.getURL());
        user.setTwitterUtcOffset(twitterUser.getUtcOffset());
        user.setTwitterVerified(twitterUser.isVerified());

        Set<WithheldCountry> withheldCountries = new HashSet<>();
        String[] fromTwitterWithheldCountries = twitterUser.getWithheldInCountries();
        if (fromTwitterWithheldCountries != null) {
            for (String curr : fromTwitterWithheldCountries) {
                WithheldCountry newCountry =
                        withheldCountryDao.createOrUpdate(curr);
                withheldCountries.add(newCountry);
            }

        }
        user.setTwitterWithheldInCountries(withheldCountries);

        return user;
    }

    public void updateUpdated(User user) {
        user.setUpdatedAt(new Date());
    }
}