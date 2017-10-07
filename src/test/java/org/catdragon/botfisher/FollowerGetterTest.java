package org.catdragon.botfisher;
import org.catdragon.botfisher.hibernate.dao.DaoFactory;
import org.catdragon.botfisher.hibernate.dao.UserDao;
import org.catdragon.botfisher.hibernate.pojo.User;
import org.catdragon.botfisher.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import twitter4j.TwitterException;

import java.io.File;
import java.io.IOException;


public class FollowerGetterTest {
    UserDao userDao;
    @Before
    public void setup() {
        DaoFactory factory = DaoFactory.instance(DaoFactory.HIBERNATE);
        userDao =
                factory.getUserDao();
    }

    @Test
    public void test() throws TwitterException, InterruptedException {
        FollowerGetter followerGetter = new FollowerGetter();
        followerGetter.doTheThing("AP", 1, 400);

    }

    @Test
    public void testOther()
            throws TwitterException, InterruptedException, IOException, ClassNotFoundException {

        File apGold = TestUtils.getFileInTargetDir("test-classes/testGold/APUser.ser");
        twitter4j.User apUser = (twitter4j.User) TestUtils.deserialize(apGold);
        User ap = userDao.create(apUser);
        ap.setCursor(-1);
        ap.setIter(4900);
        userDao.update(ap);

        FollowerGetter followerGetter = new FollowerGetter();
        followerGetter.doTheThing("AP", 5, 500);

    }


}
