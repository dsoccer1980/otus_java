package ru.dsoccer1980.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystem;


@Service
@Data
@RequiredArgsConstructor
public class FrontendMessageSystemClientImpl implements FrontendServiceMessageSystemClient {

    private final Address address = new Address("Frontend");
    private final MessageSystem messageSystem;

    @Autowired
    private SimpMessagingTemplate webSocket;


    @Override
    public void addedUser(User user) {
        webSocket.convertAndSend("/topic/response", user);
    }
}