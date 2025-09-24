package com.cts.libraryManagementConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class LibrarymanagementconfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarymanagementconfigApplication.class, args);
	}

}
