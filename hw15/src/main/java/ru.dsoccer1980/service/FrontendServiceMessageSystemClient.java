package ru.dsoccer1980.service;


import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.MessageSystemClient;

public interface FrontendServiceMessageSystemClient extends MessageSystemClient {
    void addedUser(User user);
}