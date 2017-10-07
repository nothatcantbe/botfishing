package org.catdragon.botfisher.util;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConstantsTest {
    @Test
    public void testEnv() {
        Map<String, String> env = System.getenv();
        assertThat(env.get("BLARGITYBLOOPSADADFSFS"), is(nullValue()));
        assertThat(env.get(Constants.TWITTER_CONSUMER_KEY_ENV), is(notNullValue()));
        assertThat(env.get(Constants.TWITTER_CONSUMER_SECRET_ENV), is(notNullValue()));
        assertThat(env.get(Constants.TWITTER_ACCESS_TOKEN_ENV), is(notNullValue()));
        assertThat(env.get(Constants.TWITTER_ACCESS_TOKEN_SECRET_ENV), is(notNullValue()));

    }
}