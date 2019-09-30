package ru.dsoccer1980.service;

import com.google.gson.Gson;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.dsoccer1980.domain.User;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystem;
import ru.dsoccer1980.messageSystem.message.Message;
import ru.dsoccer1980.messageSystem.message.MessageDto;
import ru.dsoccer1980.messageSystem.message.ShowAddedUserToFrontendMsg;
import ru.dsoccer1980.messageSystem.message.ToFrontendMsg;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


@Data
public class FrontendMessageSystemClientImpl implements FrontendMessageSystemClient {

    private static final int PORT = 5050;
    private static final String HOST = "localhost";

    private final MessageSystem messageSystem;
    private Address address;
    @Autowired
    private SimpMessagingTemplate webSocket;

    private PrintWriter out;

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

    @Override
    public void sendMessage(Message message) {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        out.println(json);
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @PostConstruct
    private void init() {

        Thread thread = new Thread(() -> {
            try {
                try (Socket clientSocket = new Socket(HOST, PORT)) {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    this.out = out;
                    while (true) {
                        String input = null;
                        while (input == null) {
                            input = in.readLine();
                            if (input != null) {
                                ToFrontendMsg toFrontendMsg = new Gson().fromJson(input, ShowAddedUserToFrontendMsg.class);
                                toFrontendMsg.exec(this, null);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        thread.start();
    }
}