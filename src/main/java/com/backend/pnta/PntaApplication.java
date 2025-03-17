package com.backend.pnta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.backend.pnta.Services")
@ComponentScan("com.backend.pnta.Configuration")
@ComponentScan("com.backend.pnta.Controllers")
@ComponentScan("com.backend.pnta.Proxy")
public class PntaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PntaApplication.class, args);
	}

}
