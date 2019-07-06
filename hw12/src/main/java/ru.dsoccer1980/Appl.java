package ru.dsoccer1980;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.hibernate.SessionFactory;
import ru.dsoccer1980.cache.CacheEngine;
import ru.dsoccer1980.cache.CacheEngineImpl;
import ru.dsoccer1980.dao.HibernateImpl;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.Account;
import ru.dsoccer1980.domain.AddressDataSet;
import ru.dsoccer1980.domain.PhoneDataSet;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.filters.SimpleFilter;
import ru.dsoccer1980.service.HibernateUtils;
import ru.dsoccer1980.servlets.Data;
import ru.dsoccer1980.servlets.GetUsersServlet;
import ru.dsoccer1980.servlets.PrivateInfo;
import ru.dsoccer1980.servlets.PublicInfo;

import java.net.URL;
import java.util.Collections;


public class Appl {
    private final static int PORT = 8080;
    private JdbcTemplate<User> userTemplate;

    public static void main(String[] args) throws Exception {
        new Appl().start();
    }

    private void start() throws Exception {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory("hibernate.cfg.xml",
                Account.class, User.class, AddressDataSet.class, PhoneDataSet.class);

        int size = 5;
        CacheEngine<Long, User> cache = new CacheEngineImpl<>(size, 1000, 0, false);

        userTemplate = new HibernateImpl<>(sessionFactory, cache);
        User user1 = new User("User1 name", 23);
        User user2 = new User("User2 name", 39);
        userTemplate.create(user1);
        userTemplate.create(user2);


        Server server = createServer(PORT);
        server.start();
        server.join();
    }

    public Server createServer(int port) {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new PublicInfo()), "/publicInfo");
        context.addServlet(new ServletHolder(new PrivateInfo()), "/privateInfo");
        context.addServlet(new ServletHolder(new Data()), "/data/*");
        context.addServlet(new ServletHolder(new GetUsersServlet(userTemplate)), "/users");


        context.addFilter(new FilterHolder(new SimpleFilter()), "/*", null);

        Server server = new Server(port);
        server.setHandler(new HandlerList(context));

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{createResourceHandler(), createSecurityHandler(context)});
        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});

        URL fileDir = Appl.class.getClassLoader().getResource("static");
        if (fileDir == null) {
            throw new RuntimeException("File Directory not found");
        }
        resourceHandler.setResourceBase(fileDir.getPath());
        return resourceHandler;
    }

    private SecurityHandler createSecurityHandler(ServletContextHandler context) {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"user", "admin"});

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/privateInfo/*");
        mapping.setConstraint(constraint);

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        security.setAuthenticator(new BasicAuthenticator());

        URL propFile = Appl.class.getClassLoader().getResource("realm.properties");
        if (propFile == null) {
            throw new RuntimeException("Realm property file not found");
        }

        security.setLoginService(new HashLoginService("MyRealm", propFile.getPath()));
        security.setHandler(new HandlerList(context));
        security.setConstraintMappings(Collections.singletonList(mapping));

        return security;
    }
}
