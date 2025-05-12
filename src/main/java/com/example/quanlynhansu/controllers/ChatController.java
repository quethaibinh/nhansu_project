package com.example.quanlynhansu.controllers;

import com.example.quanlynhansu.models.DTO.ChatMessage;
import com.example.quanlynhansu.models.DTO.MessageDTO;
import com.example.quanlynhansu.models.entity.MessageEntity;
import com.example.quanlynhansu.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat") // client gửi tin nhắn tới "/app/chat"
    public void processMessage(@Payload ChatMessage message, Principal principal) {
        MessageDTO messageDTO = messageService.saveMesage(message, principal);

        // Gửi lại tin nhắn cho người nhận
        // đây là dòng chủ chốt để giúp người nhận nhận được tin nhắn realtime.
        // nếu người nhân cũng subcribe vào "user/queue/messages"
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/queue/messages" , messageDTO);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getChatHistory(@RequestParam String withEmail){
        return messageService.displayChatHistory(withEmail);
    }

}
