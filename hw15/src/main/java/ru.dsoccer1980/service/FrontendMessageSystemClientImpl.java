package ru.dsoccer1980.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystem;
import ru.dsoccer1980.messageSystem.message.MessageDto;


@Data
public class FrontendMessageSystemClientImpl implements FrontendMessageSystemClient {

    private final MessageSystem messageSystem;
    private Address address;
    @Autowired
    private SimpMessagingTemplate webSocket;

    public FrontendMessageSystemClientImpl(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    @Override
    public void addedUser(User user) {
        webSocket.convertAndSend("/topic/response", user);
    }

    @Override
    public void errorHandler(Exception exception) {
        webSocket.convertAndSend("/topic/response2", new MessageDto(exception.getMessage()));
    }

}