package me.bernkastel.smokers.dto;

public class StateDto {
    private boolean isRunning;
    private int smokingTime;
    private int checkTime;

    public StateDto(boolean isRunning, int smokingTime, int checkTime) {
        this.isRunning = isRunning;
        this.smokingTime = smokingTime;
        this.checkTime = checkTime;
    }

    public StateDto() {
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public int getSmokingTime() {
        return smokingTime;
    }

    public void setSmokingTime(int smokingTime) {
        this.smokingTime = smokingTime;
    }

    public int getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(int checkTime) {
        this.checkTime = checkTime;
    }
}
