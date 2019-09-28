package ru.dsoccer1980.messageSystem;

import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.dsoccer1980.messageSystem.message.AddUserToDBMsg;
import ru.dsoccer1980.messageSystem.message.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class MessageSystemImpl implements MessageSystem {

    private static final int PORT_F = 5050;
    private static final int PORT_DB = 5051;
    private final int NUMBER_THREAD_IN_POOL = 3;
    private final List<Thread> workers = new ArrayList<>();
    private final Map<Address, LinkedBlockingQueue<Message>> messagesQueues = new HashMap<>();
    private final Map<Address, MessageSystemClient> clients = new HashMap<>();
    private final LinkedBlockingQueue<Client> frontendClients = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<ClientDB> databaseClients = new LinkedBlockingQueue<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREAD_IN_POOL);
    private Socket clientSocketDB;

    public void addMessageSystemClient(MessageSystemClient client) {
        clients.put(client.getAddress(), client);
        messagesQueues.put(client.getAddress(), new LinkedBlockingQueue<>());
    }

    public void sendMessage(Message message) {
        messagesQueues.get(message.getTo()).add(message);
    }

    public void start() {
        server();
        for (Map.Entry<Address, MessageSystemClient> entry : clients.entrySet()) {
            String name = "MessageSystem-worker-" + entry.getKey().getId();
            Thread thread = new Thread(() -> {
                while (true) {
                    LinkedBlockingQueue<Message> queue = messagesQueues.get(entry.getKey());
                    Message message = null;
                    try {
                        message = queue.take();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (message != null) {
                            message.setException(e);
                            message.errorHandler(entry.getValue());
                        }
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

    private void server() {
        Thread thread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT_F)) {
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("connected: " + clientSocket);
                    Client client = new Client(clientSocket);
                    frontendClients.put(client);
                    executorService.execute(client);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("server closed");
        });
        thread.start();

        Thread threadDb = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT_DB)) {
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    clientSocketDB = clientSocket;
                    System.out.println("connected: " + clientSocket);
                    ClientDB client = new ClientDB(clientSocket);
                    databaseClients.put(client);
                    executorService.execute(client);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("server closed");
        });
        threadDb.start();

    }

    private void clientHandler(Socket clientSocket) {

        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            //      while (!clientSocket.isClosed()) {
            String input = null;
            while (input == null) {
                input = in.readLine();
                if (input != null) {
                    // out.println("echo:" + input);
                    System.out.println(">>>" + input);
                    Gson gson = new Gson();
                    Message message = gson.fromJson(input, AddUserToDBMsg.class);
                    message.exec(clients.get(message.getTo()), out);
                }
                //}
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("clientSocket closed");
    }

    private class Client implements Runnable {
        private Socket clientSocket;
        @Getter
        private PrintWriter out;
        @Getter
        private BufferedReader in;

        Client(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                this.out = out;
                this.in = in;
                while (true) {
                    String input = null;
                    while (input == null) {
                        input = in.readLine();
                        if (input != null) {
                            System.out.println(">>>From frontend:" + input);
                            ClientDB client = databaseClients.peek();
                            if (client != null) {
                                client.out.println(input);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            System.out.println("clientSocket closed");
        }
    }

    private class ClientDB implements Runnable {
        private Socket clientSocket;
        @Getter
        private PrintWriter out;
        @Getter
        private BufferedReader in;

        ClientDB(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {
                this.out = out;
                this.in = in;
                while (true) {
                    String input = null;
                    while (input == null) {
                        input = in.readLine();
                        if (input != null) {
                            System.out.println("From db:" + input);
                            Client client = frontendClients.peek();
                            if (client != null) {
                                client.out.println(input);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            System.out.println("clientSocket closed");
        }
    }

}
