package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.io.IOException;

@SpringBootApplication
@Slf4j
public class DemoApplication {

	public static void main(String[] args) {
		Environment environment = SpringApplication.run(DemoApplication.class, args).getEnvironment();
		int port = Integer.parseInt(environment.getProperty("server.port", "8080"));
		openSwaggerUI(port);
	}

	private static void openSwaggerUI(int port) {
		try {
			Runtime runtime = Runtime.getRuntime();
			String url = String.format("http://localhost:%d/swagger-ui.html", port);
			if (System.getProperty("os.name").toLowerCase().contains("win")) {
				runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
			} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
				runtime.exec("open " + url);
			} else {
				runtime.exec("xdg-open " + url);
			}
		} catch (IOException e) {
			log.error("Error in process run Swagger Ui", e);
		}
	}
}