package ru.dsoccer1980.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.MessageSystem;
import ru.dsoccer1980.messageSystem.message.AddUserToDBMsg;

@Controller
public class MessageController {

    @Autowired
    private MessageSystem messageSystem;

    @MessageMapping("/user")
    @SendTo("/topic/response")
    public void putUser(User user) {
        messageSystem.sendMessage(new AddUserToDBMsg(user));
    }

}
