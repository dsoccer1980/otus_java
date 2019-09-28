package ru.dsoccer1980.messageSystem.message;

import lombok.Getter;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.service.DBServiceMessageSystemClient;

import java.io.PrintWriter;

public class AddUserToDBMsg extends ToDBMsg {

    @Getter
    private final User user;
    @Getter
    private Address sentAddress;
    private Exception exception;

    public AddUserToDBMsg(User user, Address address, Address sentAddress) {
        super(address);
        this.user = user;
        this.sentAddress = sentAddress;
    }

    @Override
    protected void exec(DBServiceMessageSystemClient client, PrintWriter out) {
        client.saveUser(user);
    }

    @Override
    protected void errorHandler(DBServiceMessageSystemClient client) {
        client.getMessageSystem().sendMessage(new ErrorToFrontendMsg(exception, sentAddress));
    }

    @Override
    public String toString() {
        return "AddUserToDBMsg{" +
                "user=" + user +
                ", sentAddress=" + sentAddress +
                ", exception=" + exception +
                ", to=" + to +
                '}';
    }

    @Override
    public void setException(Exception exception) {
        this.exception = exception;
    }
}
