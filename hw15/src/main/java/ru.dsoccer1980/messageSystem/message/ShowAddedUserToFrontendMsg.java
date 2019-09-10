package ru.dsoccer1980.messageSystem.message;


import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.service.FrontendServiceMessageSystemClient;

public class ShowAddedUserToFrontendMsg extends ToFrontendMsg {

    private final User user;

    public ShowAddedUserToFrontendMsg(User user, Address address) {
        super(address);
        this.user = user;
    }

    @Override
    protected void exec(FrontendServiceMessageSystemClient frontendService) {
        frontendService.addedUser(user);
    }

}