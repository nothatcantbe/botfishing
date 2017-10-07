package org.catdragon.botfisher.util;

public class Constants {
    public static final String TWITTER_CONSUMER_KEY_ENV = "twitter4j_oauth_consumerKey";
    public static final String TWITTER_CONSUMER_SECRET_ENV = "twitter4j_oauth_consumerSecret";
    public static final String TWITTER_ACCESS_TOKEN_ENV = "twitter4j_oauth_accessToken";
    public static final String TWITTER_ACCESS_TOKEN_SECRET_ENV = "twitter4j_oauth_accessTokenSecret";

    public static final String TWITTER_CONSUMER_KEY = System.getenv(TWITTER_CONSUMER_KEY_ENV);
    public static final String TWITTER_CONSUMER_SECRET = System.getenv(TWITTER_CONSUMER_SECRET_ENV);
    public static final String TWITTER_ACCESS_TOKEN = System.getenv(TWITTER_ACCESS_TOKEN_ENV);
    public static final String TWITTER_ACCESS_TOKEN_SECRET = System.getenv(TWITTER_ACCESS_TOKEN_SECRET_ENV);
}
