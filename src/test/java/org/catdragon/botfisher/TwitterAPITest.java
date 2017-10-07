package org.catdragon.botfisher;

import org.catdragon.botfisher.util.Constants;
import org.catdragon.botfisher.util.TestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import twitter4j.IDs;
import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twitter4j.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TwitterAPITest {
    /**
     * before a test can run we need to have access to the environment
     * variables that make up the twitter api access
     */
    @Test
    public void testEnvVariables() {
        assertThat(Constants.TWITTER_ACCESS_TOKEN.length(), is(Matchers.greaterThan(0)));
        assertThat(Constants.TWITTER_ACCESS_TOKEN_SECRET.length(), is(Matchers.greaterThan(0)));
        assertThat(Constants.TWITTER_CONSUMER_KEY.length(), is(Matchers.greaterThan(0)));
        assertThat(Constants.TWITTER_CONSUMER_SECRET.length(), is(Matchers.greaterThan(0)));
    }

    @Test
    public void testGetUserFromName() throws TwitterException, IOException {
        TwitterAPI twitterAPI = new TwitterAPI();
        User user = twitterAPI.getUser("AP");
        assertThat(user.getId(), is(equalTo(51241574l)));
        TestUtils.serialize(new File("APUser.ser"), user);
    }

    @Test
    public void testGetUserFromId() throws TwitterException {
        TwitterAPI twitterAPI = new TwitterAPI();
        User user = twitterAPI.getUser(51241574l);
        assertThat(user.getScreenName(), is(equalTo("AP")));
    }

    @Test
    public void testGetFollowers() throws TwitterException {
        TwitterAPI twitterAPI = new TwitterAPI();
        IDs ids = twitterAPI.getFollowers("AP", -1);
        RateLimitStatus limit = ids.getRateLimitStatus();
        if (limit.getRemaining() == 0) {

        }
    }

    @Test
    public void testGetFollowersCursor()
            throws TwitterException, FileNotFoundException, InterruptedException {
        TwitterAPI twitterAPI = new TwitterAPI();
        Set<Long> idSet = new HashSet<>();
        IDs ids = twitterAPI.getFollowers("AP", 1580488854907785702l);
        int totalIds = ids.getIDs().length;
        String followers = "";

        System.out.println("first");
        System.out.println(ids.getIDs()[0]);
        System.out.println(ids.getIDs()[1]);
        System.out.println(ids.getIDs()[2]);
        System.out.println(ids.getIDs()[3]);
        System.out.println(ids.getIDs()[4]);

        for (long id: ids.getIDs()) {
            idSet.add(id);
            followers += id + System.getProperty("line.separator");
        }

        Thread.sleep(5000);
        TestUtils.writeStringToFile(followers, new File("apFollowersEmptyCursor.txt"));
        assertThat(5000, equalTo(totalIds));
        assertThat(5000, equalTo(idSet.size()));

        long nextCursor = ids.getNextCursor();
        followers = "";
        ids = twitterAPI.getFollowers("AP", 1580488854907785702l);
        System.out.println("second");
        System.out.println(ids.getIDs()[0]);
        System.out.println(ids.getIDs()[1]);
        System.out.println(ids.getIDs()[2]);
        System.out.println(ids.getIDs()[3]);
        System.out.println(ids.getIDs()[4]);


        for (long id: ids.getIDs()) {
            idSet.add(id);
            followers += id + System.getProperty("line.separator");
        }

        TestUtils.writeStringToFile(followers, new File("apFollowersCursor"+nextCursor+".txt"));
        assertThat(5000, equalTo(idSet.size()));


    }

    @Test (expected = TwitterException.class)
    public void testGetEmptyList() throws TwitterException {
        TwitterAPI api = new TwitterAPI();
        long[] users = new long[2];
        api.getUsers(users);
    }

    @Test
    public void testGetAPFollowersFromFile() throws IOException {
        File apGold = TestUtils.getFileInTargetDir("test-classes/testGold/apFollowersGold.txt");
        String[] apFollowers = TestUtils.readStringFromFile(apGold).split(System.getProperty("line.separator"));
        ArrayList<Long> apFollowerIds = new ArrayList<>();
        for (String apFollower:apFollowers) {
            apFollowerIds.add(Long.parseLong(apFollower));
        }
        assertThat(apFollowerIds.size(), is(equalTo(apFollowers.length)));
    }


}
