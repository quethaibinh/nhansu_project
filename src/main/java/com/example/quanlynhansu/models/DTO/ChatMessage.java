package com.example.quanlynhansu.models.DTO;

import java.time.LocalTime;

public class ChatMessage {

    private String receiver; // username receiver
    private String content;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
