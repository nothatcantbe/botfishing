package org.catdragon.botfisher;

import com.google.common.primitives.Longs;
import org.apache.log4j.Logger;
import org.catdragon.botfisher.hibernate.dao.DaoFactory;
import org.catdragon.botfisher.hibernate.dao.FollowerDao;
import org.catdragon.botfisher.hibernate.dao.UserDao;
import org.catdragon.botfisher.hibernate.pojo.Follower;
import org.catdragon.botfisher.hibernate.pojo.User;
import twitter4j.IDs;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.TwitterException;

import java.util.ArrayList;

public class FollowerGetter {
    private static final Logger logger = Logger.getLogger(FollowerGetter.class);

    private UserDao userDao;
    private FollowerDao followerDao;

    int newFollowers = 0;

    public FollowerGetter() {
        DaoFactory factory = DaoFactory.instance(DaoFactory.HIBERNATE);
        userDao = factory.getUserDao();
        followerDao = factory.getFollowerDao();
    }

    public void doTheThing(String screenName, int cursors, int totalRecords)
            throws TwitterException, InterruptedException {
        newFollowers = 0;
        logger.info("getting the twitter api");
        TwitterAPI twitterAPI = new TwitterAPI();
        User user = userDao.read(screenName);
        if (user == null) {
            twitter4j.User tu = twitterAPI.getUser(screenName);
            user = userDao.create(tu);
        }


        for (int i = 0; i < cursors; i++) {
            long cursor = user.getCursor();
            logger.info(String.format("cursor %s of %s", i, cursors));

            IDs ids = null;

            for (int attempt = 2; attempt >= 0; attempt--)
            try {
                ids = twitterAPI.getFollowers(screenName, cursor);
                if (ids.getIDs().length == 0) {
                    logger.info("No more followers");
                    return;
                }
                break;
            } catch (Exception e) {
                logger.info("attempt failed", e);
                if (attempt <= 0) {
                    logger.error("failing");
                    return;
                }

                logger.info(String.format("sleeping for 5 min, %s attempts left", attempt));
                Thread.sleep(300000);
            }
            int iter = user.getIter();
            while (iter < ids.getIDs().length) {
                ArrayList<Long> candidates = new ArrayList<>();
                iter = getCandidates(candidates, ids.getIDs(), iter, user);
                if (candidates.size() == 0) {
                    logger.error(String.format("no candidates?", cursor, iter ));
                }
                ResponseList<twitter4j.User> responseList =
                        twitterAPI.getUsers(Longs.toArray(candidates));

                logger.info(String.format("adding %s new followers", responseList.size()));
                for (int j = 0; j < responseList.size(); j++) {
                    User follower = userDao.create(responseList.get(j));
                    Follower f = new Follower();
                    f.setFollowee(user);
                    f.setFollower(follower);
                    followerDao.create(f);
                }


                newFollowers += candidates.size();
                user.setIter(iter);
                if (newFollowers > totalRecords) {
                    logger.info(String.format("User imposed limit of %s reached", totalRecords));
                    return;
                }

                RateLimitStatus innerLimit = responseList.getRateLimitStatus();

                logger.info(String.format("Inner Rate limit status: [ %s ] calls remaining, [ %s ] seconds until reset", innerLimit.getRemaining(), innerLimit.getSecondsUntilReset()));
                if (innerLimit.getRemaining() == 0) {
                    logger.info("no more api calls. sleeping until reset");
                    for (int sleeping =
                         innerLimit.getSecondsUntilReset() + 1; sleeping > 0;
                         sleeping -= 10) {
                        logger.info(String.format("sleeping %s left", sleeping));
                        Thread.sleep(10000l);
                    }
                }
            }

            user.setCursor(ids.getNextCursor());
            user.setIter(0);
            userDao.update(user);

            if (user.getCursor() == 0) {
                logger.info("got everything! quitting");
            }

            RateLimitStatus limit = ids.getRateLimitStatus();
            logger.info(String.format("Rate limit status: [ %s ] calls remaining, [ %s ] seconds until reset", limit.getRemaining(), limit.getSecondsUntilReset()));
            if (limit.getRemaining() == 0) {
                logger.info("no more api calls. sleeping until reset");
                for (int sleeping =
                     limit.getSecondsUntilReset() + 1; sleeping > 0;
                     sleeping -= 10) {
                    logger.info(String.format("sleeping %s left", sleeping));
                    Thread.sleep(10000l);
                }
            }
        }
    }

    public int getCandidates(ArrayList<Long> candidates, long[] fromTwitter, int i, User user) {
        int followersAlreadyInDb = 0;
        int followersAlreadyFollowers = 0;

        while (candidates.size() < 100 && i < fromTwitter.length) {

            User follower = userDao.readTwitterId(fromTwitter[i]);
            if (follower == null) {
                candidates.add(fromTwitter[i]);
            } else {
                followersAlreadyInDb++;
                Follower f = followerDao.read(user, follower);
                if (f == null) {
                    f = new Follower();
                    f.setFollower(follower);
                    f.setFollowee(user);
                    followerDao.create(f);
                    newFollowers++;
                    followersAlreadyFollowers++;
                }
            }
            i++;
        }

        logger.info(String.format("Got %s candidates, %s followers already in db, %s followers were already marked as followers",
                candidates.size(), followersAlreadyInDb, followersAlreadyFollowers));
        return i;
    }

    // generic to wait until we can call again
    public void sleepUntilGood() {

    }
}
