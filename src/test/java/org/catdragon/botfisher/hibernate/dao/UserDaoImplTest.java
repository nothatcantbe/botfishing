package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.pojo.User;
import org.catdragon.botfisher.hibernate.pojo.WithheldCountry;
import org.catdragon.botfisher.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserDaoImplTest {
    UserDao userDao;
    WithheldCountryDao withheldCountryDao;

    @Before
    public void setup() {
        DaoFactory factory = DaoFactory.instance(DaoFactory.HIBERNATE);
        userDao = factory.getUserDao();
        withheldCountryDao = factory.getWithheldCountryDao();
    }

    @Test
    public void testNewUser() {
        User user = new User();
        user.setTwitterId(4l);
        user.setTwitterScreenName("blarg");
        User created = userDao.create(user);
        assertThat(created, is(not(nullValue())));
        user.getId();
        userDao.delete(user.getTwitterScreenName());
        User read = userDao.read(user.getTwitterScreenName());
        assertThat(read, is(nullValue()));
    }

    @Test
    public void testNewUserFromTwitterUser()
            throws IOException, ClassNotFoundException {
        File apGold = TestUtils.getFileInTargetDir("test-classes/testGold/APUser.ser");
        twitter4j.User apUser = (twitter4j.User) TestUtils.deserialize(apGold);
        User user = userDao.create(apUser);
        assertThat(user.getId(), is(not(equalTo(0))));
        assertThat(user.getTwitterScreenName(), is(equalTo(apUser.getScreenName())));
        userDao.delete(user);
    }

    @Test
    public void testNewUserFromTwitterUserWithheldCountries()
            throws IOException, ClassNotFoundException {
        File apGold = TestUtils.getFileInTargetDir("test-classes/testGold/APUser.ser");
        twitter4j.User apUser = (twitter4j.User) TestUtils.deserialize(apGold);
        User user = userDao.create(apUser);
        assertThat(user.getId(), is(not(equalTo(0))));
        assertThat(user.getTwitterScreenName(), is(equalTo(apUser.getScreenName())));

        userDao.delete(user);
        User notThere = userDao.read(user.getTwitterScreenName());
        assertThat(notThere, is(nullValue()));
        HashSet<WithheldCountry> withheldCountries = new HashSet<>();
        WithheldCountry newCountry = withheldCountryDao.createOrUpdate("GA");
        withheldCountries.add(newCountry);
        newCountry = withheldCountryDao.createOrUpdate("GB");
        withheldCountries.add(newCountry);
        user.setTwitterWithheldInCountries(withheldCountries);

        userDao.create(user);

        User read = userDao.readTwitterId(user.getTwitterId());
        assertThat(read.getTwitterWithheldInCountries().size(), equalTo(2));

        userDao.delete(read);
        withheldCountryDao.delete("GB");
        withheldCountryDao.delete("GA");
    }

    @Test
    public void testReadAll() throws IOException, ClassNotFoundException {
        long start = System.currentTimeMillis();

        File apGold = TestUtils.getFileInTargetDir("test-classes/testGold/APUser.ser");
        twitter4j.User apUser = (twitter4j.User) TestUtils.deserialize(apGold);
        User user = userDao.create(apUser);

        assertThat(start, is(lessThanOrEqualTo(user.getUpdatedAt().getTime())));

        List<User> users = userDao.readAll();
        assertThat(users.size(), is(greaterThanOrEqualTo(1)));
        userDao.delete(apUser.getScreenName());

    }

    @Test
    public void testCreateOrUpdateFromTwitterUser()
            throws IOException, ClassNotFoundException {
        File apGold = TestUtils.getFileInTargetDir("test-classes/testGold/APUser.ser");
        twitter4j.User apUser = (twitter4j.User) TestUtils.deserialize(apGold);
        User user = userDao.createOrUpdate(apUser);
        assertThat(user.getId(), is(not(equalTo(0))));
        assertThat(user.getTwitterScreenName(), is(equalTo(apUser.getScreenName())));
        assertThat(user.getTwitterWithheldInCountries().size(), is(equalTo(0)));

        HashSet<WithheldCountry> withheldCountries = new HashSet<>();
        WithheldCountry newCountry = withheldCountryDao.createOrUpdate("GA");
        withheldCountries.add(newCountry);
        newCountry = withheldCountryDao.createOrUpdate("GB");
        withheldCountries.add(newCountry);
        user.setTwitterWithheldInCountries(withheldCountries);

        user = userDao.createOrUpdate(user);

        assertThat(user.getTwitterWithheldInCountries().size(), is(equalTo(2)));


        userDao.delete(user);
        withheldCountryDao.delete("GB");
        withheldCountryDao.delete("GA");
    }

     @Test
    public void testUpdateFromTwitterUser()
            throws IOException, ClassNotFoundException {
        File apGold = TestUtils.getFileInTargetDir("test-classes/testGold/APUser.ser");
        twitter4j.User apUser = (twitter4j.User) TestUtils.deserialize(apGold);
        User user = userDao.create(apUser);
        assertThat(user.getId(), is(not(equalTo(0))));
        assertThat(user.getTwitterScreenName(), is(equalTo(apUser.getScreenName())));
        assertThat(user.getTwitterWithheldInCountries().size(), is(equalTo(0)));

        HashSet<WithheldCountry> withheldCountries = new HashSet<>();
        WithheldCountry newCountry = withheldCountryDao.createOrUpdate("GA");
        withheldCountries.add(newCountry);
        newCountry = withheldCountryDao.createOrUpdate("GB");
        withheldCountries.add(newCountry);
        user.setTwitterWithheldInCountries(withheldCountries);

        user = userDao.update(user);

        assertThat(user.getTwitterWithheldInCountries().size(), is(equalTo(2)));


        userDao.delete(user);
        withheldCountryDao.delete("GB");
        withheldCountryDao.delete("GA");
    }
}
