package ru.dsoccer1980.dao;

import java.sql.SQLException;
import java.util.List;

public interface JdbcTemplate<T> {

    void create(T objectData) throws SQLException;

    void update(T objectData) throws SQLException;

    T load(long id, Class<T> clazz) throws SQLException;

    List<T> getAll(Class<T> clazz);
}
