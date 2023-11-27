package com.github.PiotrDuma.foodmart;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FoodmartRestfulApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodmartRestfulApiApplication.class, args);
	}

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}
}
