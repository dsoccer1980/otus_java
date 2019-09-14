package ru.dsoccer1980.messageSystem.message;


import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystemClient;
import ru.dsoccer1980.service.DBServiceMessageSystemClient;
import ru.dsoccer1980.service.DBServiceMessageSystemClientImpl;

public abstract class ToDBMsg extends Message {

    public ToDBMsg(Address to) {
        super(to);
    }

    @Override
    public void exec(MessageSystemClient client) {
        if (client instanceof DBServiceMessageSystemClientImpl) {
            exec((DBServiceMessageSystemClientImpl) client);
        }
    }

    @Override
    public void errorHandler(MessageSystemClient client) {
        if (client instanceof DBServiceMessageSystemClientImpl) {
            errorHandler((DBServiceMessageSystemClientImpl) client);
        }
    }

    protected abstract void exec(DBServiceMessageSystemClient client);

    protected abstract void errorHandler(DBServiceMessageSystemClient client);
}