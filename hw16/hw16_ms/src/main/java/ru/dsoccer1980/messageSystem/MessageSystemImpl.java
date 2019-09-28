package ru.dsoccer1980.messageSystem;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class MessageSystemImpl implements MessageSystem {

    private final int NUMBER_THREAD_IN_POOL = 3;
    private final LinkedBlockingQueue<Client> frontendClients = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<Client> databaseClients = new LinkedBlockingQueue<>();
    @Value("${port.frontend}")
    private int PORT_F;
    @Value("${port.db}")
    private int PORT_DB;
    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREAD_IN_POOL);

    public void start() {
        server();
    }

    private void server() {
        new ServerThread(PORT_F, databaseClients, frontendClients).start();
        new ServerThread(PORT_DB, frontendClients, databaseClients).start();
    }

    private class ServerThread extends Thread {
        private int port;
        private LinkedBlockingQueue<Client> queueForSend;
        private LinkedBlockingQueue<Client> queueForReceive;

        public ServerThread(int port, LinkedBlockingQueue<Client> queueForSend, LinkedBlockingQueue<Client> queueForReceive) {
            this.port = port;
            this.queueForSend = queueForSend;
            this.queueForReceive = queueForReceive;
        }

        @Override
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("connected: " + clientSocket);
                    Client client = new Client(clientSocket, queueForSend);
                    queueForReceive.put(client);
                    executorService.execute(client);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("server closed");
        }
    }

    private class Client implements Runnable {
        private Socket clientSocket;
        private LinkedBlockingQueue<Client> queueForSend;
        @Getter
        private PrintWriter out;
        @Getter
        private BufferedReader in;

        Client(Socket clientSocket, LinkedBlockingQueue<Client> queueForSend) {
            this.clientSocket = clientSocket;
            this.queueForSend = queueForSend;
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
                            Client client = queueForSend.peek();
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
