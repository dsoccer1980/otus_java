package ru.dsoccer1980.servlets;

import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AddUserServlet extends HttpServlet {

    private JdbcTemplate<User> userTemplate;
    private final String USER_NAME = "user_name";
    private final String AGE = "age";

    public AddUserServlet(JdbcTemplate<User> userTemplate) {
        this.userTemplate = userTemplate;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String userName = Objects.requireNonNull(request.getParameter(USER_NAME));
        int age = Integer.valueOf(Objects.requireNonNull(request.getParameter(AGE)));

        try {
            userTemplate.create(new User(userName, age));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/viewUsers.html");
    }
}
