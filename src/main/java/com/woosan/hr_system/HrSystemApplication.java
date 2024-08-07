package com.woosan.hr_system;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@EnableAspectJAutoProxy
@SpringBootApplication
public class HrSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrSystemApplication.class, args);
	}

	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
		return new HiddenHttpMethodFilter();
	}

}

