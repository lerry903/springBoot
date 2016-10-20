package org.yctech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

	private static final Logger logger  = LoggerFactory.getLogger(Application.class);

	@RequestMapping("/")
	public String home(){
		return "\t Project for Spring Boot";
	}

	//启动Spring Boot项目唯一的入口
	public static void main(String[] args) {
		logger.info("Spring Boot Start.............");
		SpringApplication.run(Application.class, args);
		logger.info("Spring Boot End.............");
	}


}
