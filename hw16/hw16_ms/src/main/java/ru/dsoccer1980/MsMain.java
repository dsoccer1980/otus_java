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

    private static final String CLIENT_START_COMMAND = "java -jar ../hw16_frontend/target/fclient.jar";
    private static final int CLIENT_START_DELAY_SEC = 5;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MsMain.class, args);
        context.getBean(MessageSystem.class).start();
    }

    private static void start() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        startClient(executorService);
        executorService.shutdown();
    }

    private static void startClient(ScheduledExecutorService executorService) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(CLIENT_START_COMMAND);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);

    }
}
