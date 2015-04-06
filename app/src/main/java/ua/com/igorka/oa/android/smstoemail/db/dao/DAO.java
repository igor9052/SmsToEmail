package ua.com.igorka.oa.android.smstoemail.db.dao;

import java.util.List;

/**
 * Created by Igor Kuzmenko on 06.04.2015.
 *
 */

public interface DAO<K, T> {
    void insert(T entity);

    T select(K id);

    List<T> selectAll();

    void delete(K id);

    void update(T entity);

    void deleteAll();
}