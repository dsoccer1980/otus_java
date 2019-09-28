package ru.dsoccer1980.messageSystem.message;


import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystemClient;
import ru.dsoccer1980.service.FrontendMessageSystemClient;

import java.io.PrintWriter;

public abstract class ToFrontendMsg extends Message {

    public ToFrontendMsg(Address to) {
        super(to);
    }

    @Override
    public void exec(MessageSystemClient client, PrintWriter out) {
        if (client instanceof FrontendMessageSystemClient) {
            exec((FrontendMessageSystemClient) client, out);
        }
    }

    @Override
    public void errorHandler(MessageSystemClient client) {
        if (client instanceof FrontendMessageSystemClient) {
            errorHandler((FrontendMessageSystemClient) client);
        }
    }

    protected abstract void exec(FrontendMessageSystemClient frontendService, PrintWriter out);

    protected abstract void errorHandler(FrontendMessageSystemClient client);

}