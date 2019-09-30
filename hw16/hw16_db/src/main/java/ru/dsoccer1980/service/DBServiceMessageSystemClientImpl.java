package ru.dsoccer1980.service;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystem;
import ru.dsoccer1980.messageSystem.message.AddUserToDBMsg;
import ru.dsoccer1980.messageSystem.message.ShowAddedUserToFrontendMsg;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;


public class DBServiceMessageSystemClientImpl implements DBServiceMessageSystemClient {

    private static final int PORT = 5051;
    private static final String HOST = "localhost";
    private final MessageSystem messageSystem;
    private final JdbcTemplate<User> jdbcTemplate;
    @Setter
    @Getter
    private Address address;

    public DBServiceMessageSystemClientImpl(MessageSystem messageSystem, JdbcTemplate<User> jdbcTemplate) {
        this.messageSystem = messageSystem;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    void init() {
        Thread thread = new Thread(() -> {
            try {
                try (Socket clientSocket = new Socket(HOST, PORT)) {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    while (true) {
                        String input = null;
                        while (input == null) {
                            input = in.readLine();
                            if (input != null) {
                                Gson gson = new Gson();
                                AddUserToDBMsg message = gson.fromJson(input, AddUserToDBMsg.class);
                                message.exec(this, out);
                                ShowAddedUserToFrontendMsg msg = new ShowAddedUserToFrontendMsg(message.getUser(), message.getSentAddress());
                                out.println(new Gson().toJson(msg));

                            }
                        }
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }

    @Override
    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public void saveUser(User user) {
        try {
            jdbcTemplate.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
