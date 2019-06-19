package ru.dsoccer1980;

import ru.dsoccer1980.domain.Account;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.jdbc.JdbcTemplate;
import ru.dsoccer1980.jdbc.JdbcTemplateImpl;
import ru.dsoccer1980.service.DbUtils;

import java.sql.Connection;

public class Executor {
    private static final String URL = "jdbc:h2:mem:test";


    public static void main(String[] args) throws Exception {
        Connection connection = DbUtils.getConnection(URL);

        String sqlCreateTable = "create table user(" +
                "id bigint(20) NOT NULL auto_increment," +
                "name varchar(255)," +
                "age int(3))";
        DbUtils.createTable(connection, sqlCreateTable);
        sqlCreateTable = "create table account(" +
                "no bigint(20) NOT NULL auto_increment," +
                "type varchar(255)," +
                "rest number)";
        DbUtils.createTable(connection, sqlCreateTable);


        JdbcTemplate<User> jdbcTemplate = new JdbcTemplateImpl<>(connection, User.class);
        jdbcTemplate.create(new User(1L, "User1 name", 23));
        jdbcTemplate.create(new User(2L, "User2 name", 39));
        connection.commit();
        System.out.println(jdbcTemplate.load(2L, User.class));

        jdbcTemplate.update(new User(2L, "User2 updatedName", 41));
        connection.commit();
        System.out.println(jdbcTemplate.load(2L, User.class));

        JdbcTemplate<Account> jdbcTemplate2 = new JdbcTemplateImpl<>(connection, Account.class);
        jdbcTemplate2.create(new Account(1L, "Account1 name", 231));
        jdbcTemplate2.create(new Account(2L, "Account2 name", 391));
        connection.commit();
        System.out.println(jdbcTemplate2.load(2L, Account.class));

        jdbcTemplate2.update((new Account(2L, "Account2 updatedName", 3911)));
        connection.commit();
        System.out.println(jdbcTemplate2.load(2L, Account.class));

        connection.close();
    }


}

