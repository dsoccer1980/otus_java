package ru.dsoccer1980;

import org.hibernate.SessionFactory;
import ru.dsoccer1980.domain.Account;
import ru.dsoccer1980.domain.AddressDataSet;
import ru.dsoccer1980.domain.PhoneDataSet;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.server.ServerImpl;
import ru.dsoccer1980.service.HibernateUtils;
import ru.dsoccer1980.service.JDBCTemplateInit;

public class Executor {
    private final static int PORT = 8080;

    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory("hibernate.cfg.xml",
                Account.class, User.class, AddressDataSet.class, PhoneDataSet.class);

        new ServerImpl(new JDBCTemplateInit().init(sessionFactory), PORT).start();
    }


}

