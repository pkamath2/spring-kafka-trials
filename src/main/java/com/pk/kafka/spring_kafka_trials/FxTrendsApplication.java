package com.pk.kafka.spring_kafka_trials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FxTrendsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxTrendsApplication.class, args);
	}
}
