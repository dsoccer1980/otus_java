package ru.dsoccer1980.messageSystem;

import ru.dsoccer1980.messageSystem.message.Message;

public interface MessageSystem {

    void addMessageSystemClient(MessageSystemClient client);

    void sendMessage(Message message);

    void start();

    void dispose();
}
