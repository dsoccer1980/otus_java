package ru.dsoccer1980;

import ru.dsoccer1980.domain.Account;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.jdbc.HibernateImpl;
import ru.dsoccer1980.jdbc.JdbcTemplate;

public class Executor {
    private static final String URL = "jdbc:h2:mem:test";


    public static void main(String[] args) throws Exception {
        JdbcTemplate<User> jdbcTemplate = new HibernateImpl<>();
        jdbcTemplate.create(new User(1L, "User1 name", 23));
        jdbcTemplate.create(new User(2L, "User2 name", 39));
        System.out.println(">>>>>>>>>>>>>>" + jdbcTemplate.load(2L, User.class));

        jdbcTemplate.update(new User(2L, "User2 updatedName", 41));
        System.out.println(">>>>>>>>>>>>>>" + jdbcTemplate.load(2L, User.class));

        JdbcTemplate<Account> jdbcTemplate2 = new HibernateImpl<>();
        jdbcTemplate2.create(new Account(1L, "Account1 name", 231));
        jdbcTemplate2.create(new Account(2L, "Account2 name", 391));
        System.out.println(">>>>>>>>>>>>>>" + jdbcTemplate2.load(2L, Account.class));

        jdbcTemplate2.update((new Account(2L, "Account2 updatedName", 3911)));
        System.out.println(">>>>>>>>>>>>>>" + jdbcTemplate2.load(2L, Account.class));
    }


}

