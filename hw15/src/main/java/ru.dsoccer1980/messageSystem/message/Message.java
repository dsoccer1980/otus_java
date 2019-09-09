package ru.dsoccer1980.messageSystem.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystemClient;

@Data
@AllArgsConstructor
public abstract class Message {

    private final Address to;

    public abstract void exec(MessageSystemClient client);
}