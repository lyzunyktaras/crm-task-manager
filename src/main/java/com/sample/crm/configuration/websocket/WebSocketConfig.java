package com.sample.crm.configuration.websocket;

import com.sample.crm.util.JwtHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHelper jwtHelper;

    public WebSocketConfig(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue", "/topic");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(
                            @NonNull ServerHttpRequest request,
                            @NonNull WebSocketHandler wsHandler,
                            @NonNull Map<String, Object> attributes) {
                        String token = extractToken(request);
                        if (token != null && jwtHelper.validateJwtToken(token)) {
                            String username = jwtHelper.getUserNameFromToken(token);
                            log.info("WebSocket connection authenticated for user: {}", username);
                            return new WebSocketUser(username);
                        }
                        log.warn("Unauthorized WebSocket connection attempt");
                        return null;
                    }
                })
                .withSockJS();
    }

    private String extractToken(ServerHttpRequest request) {
        String token = request.getURI().getQuery();
        if (token != null && token.startsWith("token=")) {
            return token.substring(6);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    @AllArgsConstructor
    @Getter
    private static class WebSocketUser implements Principal {
        private final String name;
    }
}
