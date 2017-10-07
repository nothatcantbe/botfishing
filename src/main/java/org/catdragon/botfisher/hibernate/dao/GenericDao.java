package org.catdragon.botfisher.hibernate.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, ID extends Serializable> {

    T create(T entity);

    T read(ID id);

    T read(ID id, boolean lock);

    List<T> readAll();

    T update(T entity);

    T createOrUpdate(T entity);

    void delete(T entity);
}
