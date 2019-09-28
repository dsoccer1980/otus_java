package ru.dsoccer1980.messageSystem.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.dsoccer1980.messageSystem.Address;
import ru.dsoccer1980.messageSystem.MessageSystemClient;

import java.io.PrintWriter;

@Data
@AllArgsConstructor
public abstract class Message {

    @Getter
    protected final Address to;

    public abstract void exec(MessageSystemClient client, PrintWriter out);

    public abstract void setException(Exception exception);

    public abstract void errorHandler(MessageSystemClient client);
}