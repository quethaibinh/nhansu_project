package com.example.quanlynhansu.ws;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

// class để dùng principal, có username để dùng được messagingTemplate.convertAndSendToUser(
//                    username,
//                    "/queue/notifications",
//                    notification
//            );
// trong websocket.
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        String username = (String) attributes.get("username");
        System.out.println("Handshake user: " + username); // 👈 Log ra để kiểm tra
        if (username == null) {
            return null;
        }
        return () -> username; // anonymous class implement Principal
    }
}


