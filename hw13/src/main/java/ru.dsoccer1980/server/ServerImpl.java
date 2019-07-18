package ru.dsoccer1980.server;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import ru.dsoccer1980.dao.JdbcTemplate;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.servlets.AddUserServlet;
import ru.dsoccer1980.servlets.AdminServlet;
import ru.dsoccer1980.servlets.GetUsersServlet;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;


public class ServerImpl {

    private JdbcTemplate<User> userTemplate;
    private Server server;

    public ServerImpl(JdbcTemplate<User> userTemplate, int port) {
        this.userTemplate = userTemplate;
        server = createServer(port);
    }

    public Server getServer() {
        return server;
    }

    public void start() throws Exception {
        server.start();
        server.join();
    }

    private Server createServer(int port) {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new GetUsersServlet(userTemplate)), "/admin/users");
        context.addServlet(new ServletHolder(new AddUserServlet(userTemplate)), "/admin/addUser");
        context.addServlet(new ServletHolder(new AdminServlet()), "/admin");

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

        URL fileDir = ServerImpl.class.getClassLoader().getResource("static");
        if (fileDir == null) {
            throw new RuntimeException("File Directory not found");
        }
        resourceHandler.setResourceBase(URLDecoder.decode(fileDir.getPath(), StandardCharsets.UTF_8));
        return resourceHandler;
    }

    private SecurityHandler createSecurityHandler(ServletContextHandler context) {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"user", "admin"});

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/admin/*");
        mapping.setConstraint(constraint);

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        security.setAuthenticator(new BasicAuthenticator());

        URL propFile = ServerImpl.class.getClassLoader().getResource("realm.properties");
        if (propFile == null) {
            throw new RuntimeException("Realm property file not found");
        }

        security.setLoginService(new HashLoginService("MyRealm", propFile.getPath()));
        security.setHandler(new HandlerList(context));
        security.setConstraintMappings(Collections.singletonList(mapping));

        return security;
    }
}
