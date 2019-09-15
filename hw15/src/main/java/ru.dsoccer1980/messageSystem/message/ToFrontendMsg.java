package ru.dsoccer1980.messageSystem.message;


import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystemClient;
import ru.dsoccer1980.service.FrontendMessageSystemClient;

public abstract class ToFrontendMsg extends Message {

    public ToFrontendMsg(Address to) {
        super(to);
    }

    @Override
    public void exec(MessageSystemClient client) {
        if (client instanceof FrontendMessageSystemClient) {
            exec((FrontendMessageSystemClient) client);
        }
    }

    @Override
    public void errorHandler(MessageSystemClient client) {
        if (client instanceof FrontendMessageSystemClient) {
            errorHandler((FrontendMessageSystemClient) client);
        }
    }

    protected abstract void exec(FrontendMessageSystemClient frontendService);

    protected abstract void errorHandler(FrontendMessageSystemClient client);

}