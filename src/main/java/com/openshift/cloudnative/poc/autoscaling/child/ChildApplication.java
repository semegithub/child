package com.openshift.cloudnative.poc.autoscaling.child;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(repositoryFactoryBeanClass=MyRepository.class)
@SpringBootApplication
public class ChildApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChildApplication.class, args);
	}

}
