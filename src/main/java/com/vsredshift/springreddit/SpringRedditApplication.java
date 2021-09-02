package com.vsredshift.springreddit;

import com.vsredshift.springreddit.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfig.class)
public class SpringRedditApplication {

	public static void main(String[] args) {
		SpringApplication.run(com.vsredshift.springreddit.SpringRedditApplication.class, args);
	}

}
