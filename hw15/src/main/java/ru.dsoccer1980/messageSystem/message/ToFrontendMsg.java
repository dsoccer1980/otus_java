package ru.dsoccer1980.messageSystem.message;


import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystemClient;
import ru.dsoccer1980.service.FrontendServiceMessageSystemClient;

public abstract class ToFrontendMsg extends Message {

    public ToFrontendMsg(Address to) {
        super(to);
    }

    @Override
    public void exec(MessageSystemClient client) {
        if (client instanceof FrontendServiceMessageSystemClient) {
            exec((FrontendServiceMessageSystemClient) client);
        }
    }

    protected abstract void exec(FrontendServiceMessageSystemClient frontendService);

}