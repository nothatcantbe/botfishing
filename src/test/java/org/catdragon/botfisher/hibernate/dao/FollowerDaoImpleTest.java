package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.pojo.Follower;
import org.catdragon.botfisher.hibernate.pojo.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;


public class FollowerDaoImpleTest {
    UserDao userDao;
    FollowerDao followerDao;

    @Before
    public void setup() {
        DaoFactory factory = DaoFactory.instance(DaoFactory.HIBERNATE);
        userDao = factory.getUserDao();
        followerDao = factory.getFollowerDao();
    }

    @Test
    public void testNewFollower() {
        User followee = new User();
        followee.setTwitterId(4l);
        followee.setTwitterScreenName("blarg");
        followee = userDao.create(followee);

        User follower = new User();
        follower.setTwitterId(5l);
        follower.setTwitterScreenName("bloop");
        follower = userDao.create(follower);

        //bloop follows blarg
        Follower f = new Follower();
        f.setFollowee(followee);
        f.setFollower(follower);

        f = followerDao.create(f);

        User blah = userDao.read(followee.getId());
        assertThat(blah, notNullValue());
        followerDao.delete(f);

        userDao.delete(followee);
        blah = userDao.read(followee.getId());
        assertThat(blah, nullValue());

        userDao.delete(follower);
    }

    @Test
    public void testActivateDeactivate() {
        User followee = new User();
        followee.setTwitterId(4l);
        followee.setTwitterScreenName("blarg");
        followee = userDao.create(followee);

        User follower = new User();
        follower.setTwitterId(5l);
        follower.setTwitterScreenName("bloop");
        follower = userDao.create(follower);

        //bloop follows blarg
        Follower f = new Follower();
        f.setFollowee(followee);
        f.setFollower(follower);
        followerDao.createOrUpdate(f);
        f = followerDao.read(f.getId());
        assertThat(f.isActive(), is(equalTo(true)));
        followerDao.deactivate(f);
        f = followerDao.read(followee.getTwitterScreenName(), follower.getTwitterScreenName());
        assertThat(f.isActive(), is(equalTo(false)));
        followerDao.activate(f);
        f = followerDao.read(f.getId());
        assertThat(f.isActive(), is(equalTo(true)));

        assertThat(f.getCreatedAt().getTime(), lessThanOrEqualTo(f.getUpdatedAt().getTime()));

        //bloop follows blarg
        Follower backward = new Follower();
        backward.setFollowee(follower);
        backward.setFollower(followee);
        followerDao.create(backward);

        List<Follower> followerList = followerDao.readAll();
        assertThat(followerList.size(), equalTo(2));

        //delete backward
        followerDao.delete(follower.getTwitterScreenName(), followee.getTwitterScreenName());
        followerDao.delete(f);

        userDao.delete(followee);
        userDao.delete(follower);

    }
}
