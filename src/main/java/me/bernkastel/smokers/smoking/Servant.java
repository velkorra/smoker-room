package me.bernkastel.smokers.smoking;

import me.bernkastel.smokers.websocket.SimulationWebSocketHandler;

import java.util.Random;

public class Servant extends Thread {
    private final String name;
    private final Table table;
    private volatile boolean isRunning;
    private static final int CHECK_INTERVAL = 1000;
    private int putInterval = CHECK_INTERVAL;
    private final SimulationWebSocketHandler notifier;


    public Servant(String name, Table table, SimulationWebSocketHandler notifier) {
        this.name = name;
        this.table = table;
        this.isRunning = true;
        this.notifier = notifier;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                if (!table.isOccupied()) {
                    Stuff[] items = generateRandomItems();
                    table.putItems(items[0], items[1]);
                    notifier.broadcastNotification(name + " положил на стол " + items[0] + " и " + items[1]);
                }
                this.sleep(putInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private Stuff[] generateRandomItems() {
        Random random = new Random();
        Stuff[] items = new Stuff[2];
        items[0] = Stuff.values()[random.nextInt(Stuff.values().length)];
        do {
            items[1] = Stuff.values()[random.nextInt(Stuff.values().length)];
        } while (items[0] == items[1]);

        return items;
    }

    public void stopServing() {
        isRunning = false;
    }

    public void setPutInterval(int putInterval) {
        this.putInterval = putInterval;
    }

    public String getServantName() {
        return name;
    }
}
