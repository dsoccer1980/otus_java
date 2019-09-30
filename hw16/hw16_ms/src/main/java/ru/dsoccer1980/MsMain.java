package ru.dsoccer1980;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.dsoccer1980.messageSystem.MessageSystem;
import ru.dsoccer1980.runner.ProcessRunnerImpl;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MsMain {

    private static final String FRONTEND_START_COMMAND = "java -Dserver.port=8080 -jar ../hw16_frontend/target/frontclient.jar ";
    private static final String DB_START_COMMAND = "java -jar ../hw16_db/target/dbservice.jar";
    private static final int CLIENT_START_DELAY_SEC = 2;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MsMain.class, args);
        context.getBean(MessageSystem.class).start();
        start();
    }

    private static void start() {
        ScheduledExecutorService executorService1 = Executors.newSingleThreadScheduledExecutor();
        startClient(executorService1, FRONTEND_START_COMMAND);
        ScheduledExecutorService executorService2 = Executors.newSingleThreadScheduledExecutor();
        startClient(executorService2, DB_START_COMMAND);
        executorService1.shutdown();
        executorService2.shutdown();
    }

    private static void startClient(ScheduledExecutorService executorService, String command) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(command);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);

    }
}
