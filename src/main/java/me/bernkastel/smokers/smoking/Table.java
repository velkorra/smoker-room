package me.bernkastel.smokers.smoking;

import me.bernkastel.smokers.websocket.SimulationWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final List<Stuff> items;

    private final Object lock = new Object();

    private volatile boolean isOccupied;

    private final SimulationWebSocketHandler notifier;

    public Table(SimulationWebSocketHandler notifier) {
        this.items = new ArrayList<>();
        this.isOccupied = false;
        this.notifier = notifier;

    }

    public void putItems(Stuff item1, Stuff item2) {
        synchronized (lock) {
            while (isOccupied) {
                try {
                    lock.wait(SimulationConstants.DEFAULT_TIMEOUT);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }

            items.clear();
            items.add(item1);
            items.add(item2);
            isOccupied = true;

            lock.notifyAll();

            notifier.broadcastNotification("На стол положены: " + item1 + " и " + item2);
        }
    }


    public boolean canTakeItems(Stuff ownedItem) {
        synchronized (lock) {
            if (!isOccupied) return false;
            return items.size() == 2 && !items.contains(ownedItem);
        }
    }

    public void takeItems() {
        synchronized (lock) {
            if (!isOccupied) {
                throw new IllegalStateException("Стол пуст!");
            }

            items.clear();
            isOccupied = false;

            lock.notifyAll();

            notifier.broadcastNotification("Компоненты забраны со стола");
        }
    }

    public List<Stuff> getCurrentItems() {
        synchronized (lock) {
            return new ArrayList<>(items);
        }
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public List<Stuff> getItems() {
        return items;
    }
}