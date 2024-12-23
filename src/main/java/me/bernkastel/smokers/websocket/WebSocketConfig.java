package me.bernkastel.smokers.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final SimulationWebSocketHandler simulationWebSocketHandler;

    public WebSocketConfig(SimulationWebSocketHandler simulationWebSocketHandler) {
        this.simulationWebSocketHandler = simulationWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(simulationWebSocketHandler, "/simulation")
                .setAllowedOrigins("*");
    }
}