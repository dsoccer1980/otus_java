package ru.dsoccer1980.service;


import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.MessageSystemClient;

public interface FrontendMessageSystemClient extends MessageSystemClient {
    void addedUser(User user);
}