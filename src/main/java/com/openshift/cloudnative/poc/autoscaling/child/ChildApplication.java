package com.openshift.cloudnative.poc.autoscaling.child;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableJpaRepositories
@SpringBootApplication
@EnableSpringDataWebSupport
public class ChildApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChildApplication.class, args);
	}

}
