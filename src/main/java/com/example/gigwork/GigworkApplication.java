package com.example.gigwork;

import com.example.gigwork.config.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GigworkApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GigworkApplication.class);
		app.addInitializers(new DotenvConfig());
		app.run(args);
	}

}
