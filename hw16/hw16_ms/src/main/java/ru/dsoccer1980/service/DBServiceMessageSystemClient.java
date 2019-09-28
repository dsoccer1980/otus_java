package ru.dsoccer1980.service;


import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.MessageSystemClient;

public interface DBServiceMessageSystemClient extends MessageSystemClient {

    void saveUser(User user);
}
