package me.bernkastel.smokers.smoking;


import me.bernkastel.smokers.websocket.SimulationWebSocketHandler;

public class Smoker extends Thread {
    private int cigarettesSmoked = 0;

    private final String name;
    private final Stuff ownedItem;
    private boolean isSmoking;
    private final Table table;
    private volatile boolean isRunning;
    private volatile SmokerState state = SmokerState.IDLE;
    private final SimulationWebSocketHandler notifier;

    private int smokingTime;

    public Smoker(String name, Stuff ownedItem, Table table, int smokingTime, SimulationWebSocketHandler notifier) {
        this.name = name;
        this.ownedItem = ownedItem;
        this.table = table;
        this.smokingTime = smokingTime;
        this.isRunning = true;
        this.notifier = notifier;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                state = SmokerState.WAITING;
                if (table.canTakeItems(ownedItem)) {
                    table.takeItems();
                    state = SmokerState.SMOKING;
                    smoke();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void smoke() throws InterruptedException {
        cigarettesSmoked++;
        isSmoking = true;
        notifier.broadcastNotification(name + " начал курить (сигарета #" + cigarettesSmoked + ")");
        Thread.sleep(smokingTime);
        isSmoking = false;
        notifier.broadcastNotification(name + " закончил курить");
    }

    public Stuff getOwnedItem() {
        return ownedItem;
    }

    public boolean isSmoking() {
        return isSmoking;
    }

    public void setSmoking(int smokingTime) {
        this.smokingTime = smokingTime;
    }

    public void stopSmoking() {
        this.isRunning = false;
    }

    public SmokerState getSmokerState() {
        return state;
    }

    public int getCigarettesSmoked() {
        return cigarettesSmoked;
    }

    public String getSmokerName() {
        return name;
    }
}