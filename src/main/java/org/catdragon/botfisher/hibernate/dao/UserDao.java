package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.pojo.User;
import org.catdragon.botfisher.hibernate.util.DaoUtil;
import org.hibernate.Session;

public interface UserDao extends GenericDao<User, Long>{
    public static final String ID_FIELD = "id";
    public static final String TWITTER_SCREEN_NAME_FIELD = "twitterScreenName";
    public static final String TWITTER_ID_FIELD = "twitterId";

    public static final String ID_FIELD_DB = DaoUtil.getTableName(User.class, "getId");
    public final String TWITTER_SCREEN_NAME_FIELD_DB = DaoUtil.getTableName(User.class, "getTwitterScreenName");
    public final String TWITTER_ID_FIELD_DB = DaoUtil.getTableName(User.class, "getTwitterId");

    User create(twitter4j.User twitterUser);
    User createOrUpdate(twitter4j.User user);
    User read(String twitterScreenName);
    User readTwitterId(Long twitterId);

    void delete(String twitterScreenName);
}
