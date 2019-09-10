package ru.dsoccer1980.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystem;

import javax.annotation.PostConstruct;


@Service
@Data
public class FrontendMessageSystemClientImpl implements FrontendServiceMessageSystemClient {

    private final Address address = new Address("Frontend");
    private final MessageSystem messageSystem;
    @Autowired
    private SimpMessagingTemplate webSocket;

    public FrontendMessageSystemClientImpl(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    @Override
    public void addedUser(User user) {
        webSocket.convertAndSend("/topic/response", user);
    }

    @PostConstruct
    private void init() {
        messageSystem.addMessageSystemClient(this);
    }
}