package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
public class SmPlatformApplication {
	public static void main(String[] args) {

		System.out.println("hello world");
		SpringApplication.run(SmPlatformApplication.class, args);
	}
}
