package org.yctech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private static final Logger logger  = LoggerFactory.getLogger(Application.class);

	//启动Spring Boot项目唯一的入口
	public static void main(String[] args) {
		logger.info("Spring Boot Start.............");
		SpringApplication.run(Application.class, args);
		logger.info("Spring Boot End.............");
	}


}
