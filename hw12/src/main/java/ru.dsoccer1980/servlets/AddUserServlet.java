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

    public AddUserServlet(JdbcTemplate<User> userTemplate) {
        this.userTemplate = userTemplate;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = Objects.requireNonNull(request.getParameter("user_name"));
        int age = Integer.valueOf(Objects.requireNonNull(request.getParameter("age")));

        try {
            userTemplate.create(new User(userName, age));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/viewUsers.html");
    }
}
