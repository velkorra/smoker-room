package me.bernkastel.smokers.dto;

public class WebSocketMessage<T>{
    private String type;
    private T payload;

    public WebSocketMessage(String type, T payload) {
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public T getPayload() {
        return payload;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
