package ru.dsoccer1980.service;

import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.User;

import java.sql.SQLException;

public class InitDBData {

    public void init(JdbcTemplate<User> userTemplate) {
        User user1 = new User("User1 name", 23);
        User user2 = new User("User2 name", 39);
        try {
            userTemplate.create(user1);
            userTemplate.create(user2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
