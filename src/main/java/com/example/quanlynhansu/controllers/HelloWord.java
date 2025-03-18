package com.example.quanlynhansu.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloWord {

    @GetMapping("/hello")
    public String hello(){
        return "hello hihihi!";
    }

}
