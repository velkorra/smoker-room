package me.bernkastel.smokers.smoking;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.bernkastel.smokers.websocket.SimulationWebSocketHandler;
import me.bernkastel.smokers.dto.SimulationDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class SmokersSimulation {
    private final Table table;
    private final List<Smoker> smokers;
    private final Servant servant;
    private static final Logger logger = Logger.getLogger(SmokersSimulation.class.getName());
    private final SimulationWebSocketHandler webSocketHandler;
    private boolean running = false;
    private Thread loggingThread;
    private int smokingTime = SimulationConstants.DEFAULT_SMOKE_TIME;
    private int checkTime = SimulationConstants.DEFAULT_CHECK_INTERVAL;


    public SmokersSimulation(SimulationWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;

        this.table = new Table(webSocketHandler);
        this.smokers = new ArrayList<>();

        smokers.add(new Smoker("Курильщик с табаком", Stuff.TOBACCO, table, smokingTime, webSocketHandler));
        smokers.add(new Smoker("Курильщик с бумагой", Stuff.PAPER, table, smokingTime, webSocketHandler));
        smokers.add(new Smoker("Курильщик со спичками", Stuff.MATCH, table, smokingTime, webSocketHandler));

        this.servant = new Servant("Слуга", table, checkTime, webSocketHandler);
    }


    public void start() {
        logger.info("Simulation started");
        running = true;
        smokers.forEach(Thread::start);
        servant.start();

        loggingThread = new Thread(() -> {
            while (running) {
                logState();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        loggingThread.start();
    }

    public void stop() {
        running = false;
        smokers.forEach(Smoker::stopSmoking);
        smokers.forEach(Smoker::interrupt);
        servant.stopServing();
        servant.interrupt();
        logger.info("Simulation stopped");
        try {
            if (loggingThread != null) {
                loggingThread.interrupt();
                loggingThread.join();
            }
            for (Smoker smoker : smokers) {
                smoker.join();
            }
            servant.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            webSocketHandler.broadcastState(new SimulationDto(this));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.info("Simulation fully stopped.");

    }

    public boolean isRunning() {
        return running;
    }

    private void logState() {
        String smokersState = smokers.stream()
                .map(s -> String.format("%s: %s (выкурено: %d)",
                        s.getName(),
                        s.getSmokerState(),
                        s.getCigarettesSmoked()))
                .collect(Collectors.joining(", "));

        logger.info(String.format(
                "Simulation state - Table: %s occupied, Items: %s, Smokers: [%s]",
                table.isOccupied(),
                table.getCurrentItems(),
                smokersState
        ));
        try {
            webSocketHandler.broadcastState(new SimulationDto(this));
        } catch (JsonProcessingException e) {
            logger.warning("Failed to broadcast state: " + e.getMessage());
        }
    }

    public void setSmokingTime(int smokingTime) {
        this.smokingTime = smokingTime;
    }

    public void setCheckTimeCurrent(int checkTime) {
        servant.setPutInterval(checkTime);
    }

    public void setDefaultCheckTime(int checkTime) {
        this.checkTime = checkTime;
    }

    public void setSmokeTime(int milliseconds) {
        smokers.forEach(s -> s.setSmoking(milliseconds));
    }

    public List<Smoker> getSmokers() {
        return new ArrayList<>(smokers);
    }

    public Table getTable() {
        return table;
    }

    public Servant getServant() {
        return servant;
    }
}