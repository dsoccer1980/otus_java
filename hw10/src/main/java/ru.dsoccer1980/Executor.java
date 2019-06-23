package ru.dsoccer1980;

import org.hibernate.SessionFactory;
import ru.dsoccer1980.domain.AddressDataSet;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.jdbc.HibernateImpl;
import ru.dsoccer1980.jdbc.JdbcTemplate;
import ru.dsoccer1980.service.DbUtils;

public class Executor {
    private static final String URL = "jdbc:h2:mem:test";


    public static void main(String[] args) throws Exception {
        SessionFactory sessionFactory = DbUtils.getSessionFactory();

        JdbcTemplate<User> userTemplate = new HibernateImpl<>(sessionFactory);
        User user1 = new User("User1 name", 23, null);
        userTemplate.create(user1);
        User user2 = new User("User2 name", 39, null);
        userTemplate.create(user2);

        System.out.println(">>>>>>>>>>>>>> User2: " + userTemplate.load(user2.getId(), User.class));

        user2.setName("User2 updatedName");
        user2.setAge(41);
        userTemplate.update(user2);
        System.out.println(">>>>>>>>>>>>>> User2 updated: " + userTemplate.load(user2.getId(), User.class));

        JdbcTemplate<AddressDataSet> addressTemplate = new HibernateImpl<>(sessionFactory);
        AddressDataSet addressDataSet = new AddressDataSet("ul. Lenina");
        addressTemplate.create(addressDataSet);
        System.out.println(">>>>>>>>>>>>>>" + addressTemplate.load(addressDataSet.getId(), AddressDataSet.class));


        User user3 = new User("User3 name", 25, addressDataSet);
        userTemplate.create(user3);
        System.out.println(">>>>>>>>>>>>>>" + userTemplate.load(user3.getId(), User.class));


        addressDataSet.setStreet("Update ul");
        addressTemplate.update(addressDataSet);

        System.out.println(">>>>>>>>>>>>>>" + userTemplate.load(user3.getId(), User.class));

     /*   JdbcTemplate<Account> jdbcTemplate2 = new HibernateImpl<>();
        jdbcTemplate2.create(new Account(1L, "Account1 name", 231));
        jdbcTemplate2.create(new Account(2L, "Account2 name", 391));
        System.out.println(">>>>>>>>>>>>>>" + jdbcTemplate2.load(2L, Account.class));

        jdbcTemplate2.update((new Account(2L, "Account2 updatedName", 3911)));
        System.out.println(">>>>>>>>>>>>>>" + jdbcTemplate2.load(2L, Account.class));*/
    }


}
