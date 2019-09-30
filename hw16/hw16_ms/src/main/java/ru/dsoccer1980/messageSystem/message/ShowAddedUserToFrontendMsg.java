package ru.dsoccer1980.messageSystem.message;


import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.service.FrontendMessageSystemClient;

import java.io.PrintWriter;

public class ShowAddedUserToFrontendMsg extends ToFrontendMsg {

    private final User user;

    private Exception exception;

    public ShowAddedUserToFrontendMsg(User user, Address address) {
        super(address);
        this.user = user;
    }

    @Override
    protected void exec(FrontendMessageSystemClient frontendService, PrintWriter out) {
        frontendService.addedUser(user);
    }

    @Override
    protected void errorHandler(FrontendMessageSystemClient client) {
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "ShowAddedUserToFrontendMsg{" +
                "user=" + user +
                ", to=" + to +
                '}';
    }
}