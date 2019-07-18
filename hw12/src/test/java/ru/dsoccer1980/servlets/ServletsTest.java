package ru.dsoccer1980.servlets;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.dsoccer1980.dao.HibernateImpl;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.Account;
import ru.dsoccer1980.domain.AddressDataSet;
import ru.dsoccer1980.domain.PhoneDataSet;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.server.ServerImpl;
import ru.dsoccer1980.service.HibernateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ServletsTest {
    private final static int PORT = 8081;
    private static Server server;

    @AfterEach
    void stopServer() throws Exception {
        server.stop();
    }

    @BeforeEach
    void startServer() throws Exception {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory("hibernate.cfg.xml",
                Account.class, User.class, AddressDataSet.class, PhoneDataSet.class);

        JdbcTemplate<User> userTemplate = new HibernateImpl<>(sessionFactory);
        User user1 = new User("User1 testname", 23);
        User user2 = new User("User2 testname", 39);
        userTemplate.create(user1);
        userTemplate.create(user2);

        server = new ServerImpl(userTemplate, PORT).getServer();
        server.start();
    }

    private URL makeUrl(String part) throws MalformedURLException {
        return new URL("http://localhost:" + PORT + part);
    }

    private HttpURLConnection getConnection(String url, String requestMethod, String username, String password) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) makeUrl(url).openConnection();
        connection.setAuthenticator(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
        connection.setRequestMethod(requestMethod);
        return connection;
    }

    @Test
    @DisplayName("test /admin/users")
    void getAllUsers() throws IOException {
        HttpURLConnection connection = getConnection("/admin/users", "GET", "admin", "pass");

        assertEquals(HttpStatus.OK_200, connection.getResponseCode(), "doGet works");

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        assertEquals("[{\"id\":1,\"name\":\"User1 testname\",\"age\":23},{\"id\":2,\"name\":\"User2 testname\",\"age\":39}]", stringBuilder.toString());
    }

    @Test
    @DisplayName("test /admin/user")
    void addUser() throws IOException {
        HttpURLConnection connection = getConnection("/admin/addUser?user_name=New&age=24", "POST", "admin", "pass");

        assertEquals(HttpStatus.OK_200, connection.getResponseCode(), "doPost works");
    }

    @Test
    @DisplayName("test /admin/users with wrong password")
    void getUsersWithWrongAuth() throws IOException {
        HttpURLConnection connection = getConnection("/admin/users", "GET", "admin", "wrong");

        assertEquals(HttpStatus.UNAUTHORIZED_401, connection.getResponseCode());

    }
}