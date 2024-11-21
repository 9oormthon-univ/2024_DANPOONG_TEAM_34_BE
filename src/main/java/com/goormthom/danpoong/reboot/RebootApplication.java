package com.goormthom.danpoong.reboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RebootApplication {

	public static void main(String[] args) {
		SpringApplication.run(RebootApplication.class, args);
	}

}
