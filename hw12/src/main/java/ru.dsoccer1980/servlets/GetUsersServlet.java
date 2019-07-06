package ru.dsoccer1980.servlets;

import com.google.gson.Gson;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GetUsersServlet extends HttpServlet {

    private JdbcTemplate<User> userTemplate;

    public GetUsersServlet(JdbcTemplate<User> userTemplate) {
        this.userTemplate = userTemplate;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<User> users = userTemplate.getAll(User.class);

        String usersListJson = new Gson().toJson(users);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(usersListJson);
        printWriter.flush();
    }
}