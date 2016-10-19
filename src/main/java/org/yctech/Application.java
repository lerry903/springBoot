package org.yctech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.yctech.common.GlobalConstant;
import org.yctech.resource.ResourceBox;

@Configuration
@EnableAutoConfiguration
@RestController
public class Application {

	public static void main(String[] args) {
        System.out.println(GlobalConstant.getConstantResource("successful",1));
        System.out.println(ResourceBox.getInstance().getResource("business_exception_100"));
		//SpringApplication.run(Application.class, args);
	}
}
