package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;

@ServletComponentScan // 开启了springboot对servlet组件的支持
@SpringBootApplication
public class WebDevelopApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebDevelopApplication.class, args);
	}

}
