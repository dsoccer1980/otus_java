package ru.dsoccer1980.messageSystem;

import ru.dsoccer1980.messageSystem.message.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageSystemImpl implements MessageSystem {

    private final List<Thread> workers = new ArrayList<>();
    private final Map<Address, LinkedBlockingQueue<Message>> messagesQueues = new HashMap<>();
    private final Map<Address, MessageSystemClient> clients = new HashMap<>();

    public void addMessageSystemClient(MessageSystemClient client) {
        clients.put(client.getAddress(), client);
        messagesQueues.put(client.getAddress(), new LinkedBlockingQueue<>());
    }

    public void sendMessage(Message message) {
        messagesQueues.get(message.getTo()).add(message);
    }

    public void start() {
        for (Map.Entry<Address, MessageSystemClient> entry : clients.entrySet()) {
            String name = "MessageSystem-worker-" + entry.getKey().getId();
            Thread thread = new Thread(() -> {
                while (true) {
                    LinkedBlockingQueue<Message> queue = messagesQueues.get(entry.getKey());
                    Message message;
                    try {
                        message = queue.take();
                        message.exec(entry.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            });
            thread.setName(name);
            thread.start();
            workers.add(thread);
        }
    }

    public void dispose() {
        workers.forEach(Thread::interrupt);
    }

}
