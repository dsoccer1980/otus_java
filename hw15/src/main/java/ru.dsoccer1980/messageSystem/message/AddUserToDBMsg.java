package ru.dsoccer1980.messageSystem.message;

import lombok.Setter;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.service.DBServiceMessageSystemClient;

import java.sql.SQLException;

public class AddUserToDBMsg extends ToDBMsg {

    private final User user;

    private Address sentAddress;

    @Setter
    private Exception exception;

    public AddUserToDBMsg(User user, Address address, Address sentAddress) {
        super(address);
        this.user = user;
        this.sentAddress = sentAddress;
    }

    @Override
    protected void exec(DBServiceMessageSystemClient client) {
        try {
            client.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        client.getMessageSystem().sendMessage(new ShowAddedUserToFrontendMsg(user, sentAddress));
    }

    @Override
    protected void errorHandler(DBServiceMessageSystemClient client) {
        client.getMessageSystem().sendMessage(new ErrorToFrontendMsg(exception, sentAddress));
    }
}
