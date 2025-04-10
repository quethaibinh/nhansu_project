package com.example.quanlynhansu.controllers;

import com.example.quanlynhansu.models.DTO.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat") // client gửi tin nhắn tới "/app/chat"
    public void processMessage(@Payload ChatMessage message) {
        // Gửi lại tin nhắn cho người nhận
        messagingTemplate.convertAndSend("/topic/messages/" + message.getReceiver(), message);
    }

}
