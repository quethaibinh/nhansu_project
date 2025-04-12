package com.example.quanlynhansu.controllers;

import com.example.quanlynhansu.services.OnlineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/online-users")
public class OnlineUserController {

    @Autowired
    private OnlineUserService onlineUserService;

    @GetMapping
    public ResponseEntity<Set<String>> getOnlineUsers() {
        return ResponseEntity.ok(onlineUserService.getOnlineUsers());
    }
}

