package org.catdragon.botfisher.util;

import org.catdragon.botfisher.hibernate.pojo.User;
import org.catdragon.botfisher.hibernate.util.DaoUtil;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestUtilsTest {
    @Test
    public void testUtils() {
        String idTable = DaoUtil.getTableName(User.class, "getId");
        assertThat(idTable, is(notNullValue()));
        assertThat(idTable, is(equalTo("user_id")));

        String twitterScreenNameTable = DaoUtil.getTableName(User.class, "getTwitterScreenName");
        assertThat(twitterScreenNameTable, is(notNullValue()));
        assertThat(twitterScreenNameTable, is(equalTo("twitter_screen_name")));

    }

}
