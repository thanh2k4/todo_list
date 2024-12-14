package com.example.todo_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class TodoBackendApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().directory("./todo-backend").filename(".env").ignoreIfMissing().load();
		System.setProperty("DB_URL", dotenv.get("DB_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("CORS_ORIGIN", dotenv.get("CORS_ORIGIN"));
		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		SpringApplication.run(TodoBackendApplication.class, args);
	}

}
