package com.pablo.digitalstore.digital_store_api;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DigitalStoreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalStoreApiApplication.class, args);
	}

}
