package com.example.quanlynhansu.ws;

import com.example.quanlynhansu.services.OnlineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private OnlineUserService onlineUserService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() != null) {
            String username = accessor.getUser().getName();
            onlineUserService.addUser(username);
            System.out.println("User connected: " + username);
        } else {
            System.out.println("User connect failed: no principal found.");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) { //  khi client ngắt kết nối
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = accessor.getUser() != null ? accessor.getUser().getName() : null;
        if (username != null) {
            onlineUserService.removeUser(username);
        }
    }
}


// Bean WebSocketEventListener tự động được Spring gọi khi xảy ra các sự kiện như SessionConnectedEvent hay SessionDisconnectEvent,
// nhờ vào annotation @EventListener.

//Spring Framework có một Event System rất mạnh.
//Khi một hành động nào đó xảy ra (ví dụ client kết nối WebSocket), Spring sẽ phát ra một "event" (sự kiện).

//Một số sự kiện WebSocket mặc định:
    //SessionConnectedEvent	Khi client kết nối WebSocket thành công
    //SessionDisconnectEvent	Khi client ngắt kết nối WebSocket
    //SessionSubscribeEvent	Khi client subscribe vào một topic
    //SessionUnsubscribeEvent	Khi client unsubscribe khỏi một topic