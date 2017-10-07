package org.catdragon.botfisher;

import org.catdragon.botfisher.util.Constants;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterClientFactory {

    public static Twitter getTwitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET)
                .setOAuthAccessToken(Constants.TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(Constants.TWITTER_ACCESS_TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }
}
