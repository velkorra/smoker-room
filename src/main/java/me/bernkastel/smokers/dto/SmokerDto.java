package me.bernkastel.smokers.dto;

import me.bernkastel.smokers.smoking.Smoker;
import me.bernkastel.smokers.smoking.Stuff;

public class SmokerDto {
    private String name;
    private String state;
    private Stuff ownedItem;
    private int cigarettesSmoked;

    public SmokerDto(Smoker smoker) {
        this.name = smoker.getSmokerName();
        this.state = smoker.getSmokerState().toString();
        this.ownedItem = smoker.getOwnedItem();
        this.cigarettesSmoked = smoker.getCigarettesSmoked();
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Stuff getOwnedItem() {
        return ownedItem;
    }

    public void setOwnedItem(Stuff ownedItem) {
        this.ownedItem = ownedItem;
    }

    public int getCigarettesSmoked() {
        return cigarettesSmoked;
    }

    public void setCigarettesSmoked(int cigarettesSmoked) {
        this.cigarettesSmoked = cigarettesSmoked;
    }
}
