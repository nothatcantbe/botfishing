package org.catdragon.botfisher.hibernate.dao;

import org.catdragon.botfisher.hibernate.pojo.User;
import org.catdragon.botfisher.hibernate.pojo.WithheldCountry;
import org.catdragon.botfisher.hibernate.util.DaoUtil;

public interface WithheldCountryDao extends GenericDao<WithheldCountry, Integer>{

    public final String ID_FIELD = "id";
    public final String COUNTRY_CODE_FIELD = "countryCode";

    public final String ID_FIELD_DB =
            DaoUtil.getTableName(WithheldCountry.class, "getId");
    public final String COUNTRY_CODE_FIELD_DB =
            DaoUtil.getTableName(WithheldCountry.class, "getCountryCode");

    public WithheldCountry create(String countryCode);
    public WithheldCountry read(String countryCode);
    public WithheldCountry createOrUpdate(String countryCode);
    public void delete(String countryCode);
}
