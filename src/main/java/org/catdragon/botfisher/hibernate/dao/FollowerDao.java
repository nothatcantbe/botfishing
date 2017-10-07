package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.pojo.Follower;
import org.catdragon.botfisher.hibernate.pojo.User;

public interface FollowerDao extends GenericDao<Follower, Long>{
    public static final String ID_FIELD = "id";
    public static final String FOLLOWER_FIELD = "follower";
    public static final String FOLLOWEE_FIELD = "followee";

    Follower read(String followeeScreenName, String followerScreenName);
    Follower read(User followee, User follower);
    Follower activate(Follower entity);
    Follower deactivate(Follower entity);
    void delete(String followeeScreenName, String followerScreenName);
}
