package com.example.gigwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GigworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(GigworkApplication.class, args);
	}

}
