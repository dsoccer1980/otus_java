package ru.dsoccer1980.jdbc;

import java.sql.SQLException;

public interface JdbcTemplate<T> {

    void create(T objectData) throws SQLException;

    void update(T objectData) throws SQLException;

    T load(long id, Class<T> clazz) throws SQLException;
}
