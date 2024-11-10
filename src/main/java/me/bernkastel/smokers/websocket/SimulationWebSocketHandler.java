package me.bernkastel.smokers.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.bernkastel.smokers.dto.SimulationDto;
import me.bernkastel.smokers.dto.WebSocketMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class SimulationWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    public synchronized void broadcastState(SimulationDto state) throws JsonProcessingException {
        WebSocketMessage<SimulationDto> message = new WebSocketMessage<>("SIMULATION", state);
        String jsonMessage = new ObjectMapper().writeValueAsString(message);
        sessions.removeIf(session -> !session.isOpen());
        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                // handle error
            }
        });
    }

    public synchronized void broadcastNotification(String message) {
        WebSocketMessage<String> notification = new WebSocketMessage<>("NOTIFICATION", message);
        try {

            String jsonMessage = new ObjectMapper().writeValueAsString(notification);
            sessions.removeIf(session -> !session.isOpen());
            sessions.forEach(session -> {
                try {
                    session.sendMessage(new TextMessage(jsonMessage));
                } catch (IOException e) {
                    // handle error
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}