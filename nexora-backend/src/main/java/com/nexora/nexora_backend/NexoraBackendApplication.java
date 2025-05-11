package com.nexora.nexora_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.nexora.nexora_backend.repository")
//@EntityScan(basePackages = "com.nexora.nexora_backend.model")
public class NexoraBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexoraBackendApplication.class, args);
	}

}
