package ru.dsoccer1980.messageSystem.message;

import lombok.Setter;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.service.FrontendMessageSystemClient;

public class ErrorToFrontendMsg extends ToFrontendMsg {

    @Setter
    private Exception exception;

    public ErrorToFrontendMsg(Exception exception, Address address) {
        super(address);
        this.exception = exception;
    }

    @Override
    protected void exec(FrontendMessageSystemClient frontendService) {
        frontendService.errorHandler(exception);
    }

    @Override
    protected void errorHandler(FrontendMessageSystemClient client) {
        client.getMessageSystem().sendMessage(new ErrorToFrontendMsg(exception, to));
    }
}
