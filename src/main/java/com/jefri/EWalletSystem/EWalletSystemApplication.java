package com.jefri.EWalletSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EWalletSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EWalletSystemApplication.class, args);
	}

}
