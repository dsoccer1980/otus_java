package ru.dsoccer1980.messageSystem.message;

import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.service.FrontendMessageSystemClient;

import java.io.PrintWriter;

public class ErrorToFrontendMsg extends ToFrontendMsg {

    private Exception exception;

    public ErrorToFrontendMsg(Exception exception, Address address) {
        super(address);
        this.exception = exception;
    }

    @Override
    protected void exec(FrontendMessageSystemClient frontendService, PrintWriter out) {
        frontendService.errorHandler(exception);
    }

    @Override
    protected void errorHandler(FrontendMessageSystemClient client) {
        client.getMessageSystem().sendMessage(new ErrorToFrontendMsg(exception, to));
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
