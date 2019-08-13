package ru.dsoccer1980.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Controller
public class UsersController {

    private JdbcTemplate<User> userTemplate;

    public UsersController(JdbcTemplate<User> userTemplate) {
        this.userTemplate = userTemplate;
    }

    @GetMapping("/")
    public String firstPage() {
        return "viewUsers.html";
    }

    @GetMapping("/user/list")
    @ResponseBody
    public List<User> getUsers() {
        return userTemplate.getAll(User.class);
    }

    @PostMapping("/user/create")
    public RedirectView addUser(@ModelAttribute User user) throws SQLException {
        Objects.requireNonNull(user);

        userTemplate.create(user);

        return new RedirectView("/", true);

    }
}
