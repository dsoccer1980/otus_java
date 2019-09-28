package ru.dsoccer1980.messageSystem.message;


import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystemClient;
import ru.dsoccer1980.service.DBServiceMessageSystemClient;

import java.io.PrintWriter;

public abstract class ToDBMsg extends Message {

    public ToDBMsg(Address to) {
        super(to);
    }

    @Override
    public void exec(MessageSystemClient client, PrintWriter out) {
        if (client instanceof DBServiceMessageSystemClient) {
            exec((DBServiceMessageSystemClient) client, out);
        }
    }

    @Override
    public void errorHandler(MessageSystemClient client) {
        if (client instanceof DBServiceMessageSystemClient) {
            errorHandler((DBServiceMessageSystemClient) client);
        }
    }

    protected abstract void exec(DBServiceMessageSystemClient client, PrintWriter out);

    protected abstract void errorHandler(DBServiceMessageSystemClient client);
}