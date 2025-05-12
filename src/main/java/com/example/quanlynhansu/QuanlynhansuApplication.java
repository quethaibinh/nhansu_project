package com.example.quanlynhansu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class QuanlynhansuApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuanlynhansuApplication.class, args);
	}

}
