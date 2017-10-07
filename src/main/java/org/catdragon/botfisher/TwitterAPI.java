package org.catdragon.botfisher;


import org.apache.log4j.Logger;
import twitter4j.*;

import javax.xml.ws.Response;
import java.util.List;

public class TwitterAPI {
    private static final Logger logger = Logger.getLogger(TwitterAPI.class);
    private final Twitter twitter;

    public TwitterAPI() {
        this.twitter=TwitterClientFactory.getTwitter();
    }

    public User getUser(String userName) throws TwitterException {
        logger.info(String.format("get user [ %s ]", userName));
        User user = twitter.showUser(userName);
        logger.info(String.format("queried user [ %s ]", user));
        return user;
    }

    public ResponseList<User> getUsers(long[] userIds) throws TwitterException {
        logger.info("looking up users: " + userIds.length);
        ResponseList<User> userResponseList = twitter.lookupUsers(userIds);
        return userResponseList;
    }

    public User getUser(long userId) throws TwitterException {
        logger.info(String.format("get user [ %s ]", userId));
        User user = twitter.showUser(userId);
        logger.info(String.format("queried user [ %s ]", user));
        return user;
    }

    public IDs getFollowers(String username, long cursor)
            throws TwitterException {
        logger.info(String.format("Getting followers for [ %s ] at cursor [ %s ]", username, cursor));
        IDs ids = twitter.getFollowersIDs(username, cursor);
        return ids;
    }
}
