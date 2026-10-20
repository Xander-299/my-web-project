package com.tlias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;

@ServletComponentScan // 开启了springboot对servlet组件的支持
@SpringBootApplication
public class TliasApplication {

	public static void main(String[] args) {
		SpringApplication.run(TliasApplication.class, args);
	}

}
