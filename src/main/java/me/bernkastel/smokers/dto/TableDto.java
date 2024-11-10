package me.bernkastel.smokers.dto;

import me.bernkastel.smokers.smoking.Stuff;
import me.bernkastel.smokers.smoking.Table;

import java.util.List;

public class TableDto {
    private boolean isOccupied;
    private List<Stuff> items;

    public TableDto(Table table) {
        this.isOccupied = table.isOccupied();
        this.items = table.getItems();
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public List<Stuff> getItems() {
        return items;
    }

    public void setItems(List<Stuff> items) {
        this.items = items;
    }
}
