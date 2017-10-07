package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.pojo.WithheldCountry;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WithheldCountryDaoImplTest {
    WithheldCountryDao withheldCountryDao;

    @Before
    public void setup() {
        DaoFactory factory = DaoFactory.instance(DaoFactory.HIBERNATE);
        withheldCountryDao = factory.getWithheldCountryDao();
    }

    @Test
    public void testNewWithheldCountry() {
        WithheldCountry withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode("US");
        assertThat(withheldCountry.getId(), is(equalTo(0)));

        withheldCountryDao.create(withheldCountry);
        int id = withheldCountry.getId();
        assertThat(id, is(not(equalTo(0))));

        withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode("GB");
        assertThat(withheldCountry.getId(), is(equalTo(0)));

        withheldCountryDao.create(withheldCountry);
        id = withheldCountry.getId();
        assertThat(id, is(not(equalTo(0))));
    }

    @Test
    public void testNewWithheldCountryByNameEmpty() {
        WithheldCountry notThere = withheldCountryDao.read("BLARG");
        assertThat(notThere, is(nullValue()));
    }

    @Test
    public void testNewWithheldCountryByName() {
        WithheldCountry created = withheldCountryDao.create("CA");
        int id = created.getId();
        assertThat(id, is(not(equalTo(0))));

        created = withheldCountryDao.create("MX");
        id = created.getId();
        assertThat(id, is(not(equalTo(0))));
    }

    @Test(expected = PersistenceException.class)
    public void testNewWithheldCountryByNameUniqueConstrain() {
        WithheldCountry created = withheldCountryDao.create("CA");
        int id = created.getId();
        assertThat(id, is(not(equalTo(0))));

        created = withheldCountryDao.create("CA");
        id = created.getId();
    }

    @Test
    public void testReadWithheldCountry() {
        WithheldCountry withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode("FR");
        assertThat(withheldCountry.getId(), is(equalTo(0)));

        withheldCountryDao.create(withheldCountry);
        int id = withheldCountry.getId();
        assertThat(id, is(not(equalTo(0))));

        withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode("ES");
        assertThat(withheldCountry.getId(), is(equalTo(0)));

        withheldCountryDao.create(withheldCountry);
        id = withheldCountry.getId();
        assertThat(id, is(not(equalTo(0))));

        WithheldCountry fr = withheldCountryDao.read("FR");
        assertThat(fr.getCountryCode(), is(equalTo("FR")));
        WithheldCountry es = withheldCountryDao.read("ES");
        assertThat(es.getCountryCode(), is(equalTo("ES")));
        WithheldCountry it = withheldCountryDao.read("IT");
        assertThat(it, is(nullValue()));

    }

    @Test
    public void testReadWithheldCountryPriKeyAlone() {
        WithheldCountry AF = withheldCountryDao.read(10000);
        assertThat(AF, is(nullValue()));
    }

    @Test
    public void testReadWithheldCountryPriKey() {
        WithheldCountry withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode("AF");
        assertThat(withheldCountry.getId(), is(equalTo(0)));

        withheldCountryDao.create(withheldCountry);
        int idAf = withheldCountry.getId();
        assertThat(idAf, is(not(equalTo(0))));

        withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode("AL");
        assertThat(withheldCountry.getId(), is(equalTo(0)));

        withheldCountryDao.create(withheldCountry);
        int idAl = withheldCountry.getId();
        assertThat(idAl, is(not(equalTo(0))));

        WithheldCountry AF = withheldCountryDao.read(idAf);
        assertThat(AF.getCountryCode(), is(equalTo("AF")));
        WithheldCountry AL = withheldCountryDao.read(idAl);
        assertThat(AL.getCountryCode(), is(equalTo("AL")));
        WithheldCountry DZ = withheldCountryDao.read(idAl + 1000);
        assertThat(DZ, is(nullValue()));

    }

    @Test
    public void testReadAllCountries() {
        WithheldCountry withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode("AS");
        assertThat(withheldCountry.getId(), is(equalTo(0)));
        withheldCountryDao.create(withheldCountry);
        assertThat(withheldCountry.getId(), is(not(equalTo(0))));

        List<WithheldCountry> allCountries = withheldCountryDao.readAll();
        assertThat(allCountries.size(), is(greaterThanOrEqualTo(1)));
        assertThat(allCountries.contains(withheldCountry), is(true));
    }

    @Test
    public void testReadAllCountriesNoCreate() {
        List<WithheldCountry> allCountries = withheldCountryDao.readAll();
        assertThat(allCountries.size(), is(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testUpdate() {
        WithheldCountry withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode("EZ");
        assertThat(withheldCountry.getId(), is(equalTo(0)));
        withheldCountryDao.create(withheldCountry);
        assertThat(withheldCountry.getId(), is(not(equalTo(0))));

        withheldCountry.setCountryCode("ZE");
        withheldCountryDao.update(withheldCountry);

        WithheldCountry ZE = withheldCountryDao.read("ZE");
        assertThat(ZE.getCountryCode(), is(equalTo("ZE")));
        WithheldCountry EZ = withheldCountryDao.read("EZ");
        assertThat(EZ, is(nullValue()));
    }

    @Test
    public void testCreateOrUpdateAlreadyCreated() {
        WithheldCountry withheldCountry = new WithheldCountry();
        withheldCountry.setCountryCode("FA");
        assertThat(withheldCountry.getId(), is(equalTo(0)));
        withheldCountryDao.create(withheldCountry);
        assertThat(withheldCountry.getId(), is(not(equalTo(0))));

        withheldCountryDao.createOrUpdate("FA");

        WithheldCountry ZE = withheldCountryDao.read("FA");
        assertThat(ZE.getCountryCode(), is(equalTo("FA")));
    }

    @Test
    public void testCreateOrUpdateNotCreated() {
        WithheldCountry FB = withheldCountryDao.read("FB");
        assertThat(FB, is(nullValue()));

        withheldCountryDao.createOrUpdate("FB");

        FB = withheldCountryDao.read("FB");
        assertThat(FB.getCountryCode(), is(equalTo("FB")));
    }

    @Test
    public void testDeleteString() {
        WithheldCountry FC = withheldCountryDao.read("FC");
        assertThat(FC, is(nullValue()));

        withheldCountryDao.createOrUpdate("FC");

        FC = withheldCountryDao.read("FC");
        assertThat(FC.getCountryCode(), is(equalTo("FC")));

        withheldCountryDao.delete("FC");
        FC = withheldCountryDao.read("FC");

        assertThat(FC, is(nullValue()));
    }

    @Test
    public void testDelete() {
        WithheldCountry FC = withheldCountryDao.read("FC");
        assertThat(FC, is(nullValue()));

        withheldCountryDao.createOrUpdate("FC");

        FC = withheldCountryDao.read("FC");
        assertThat(FC.getCountryCode(), is(equalTo("FC")));

        withheldCountryDao.delete(FC);
        FC = withheldCountryDao.read("FC");

        assertThat(FC, is(nullValue()));
    }
}

