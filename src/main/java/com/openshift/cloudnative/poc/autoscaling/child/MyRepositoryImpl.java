package com.openshift.cloudnative.poc.autoscaling.child;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

public class MyRepositoryImpl  {

	@Autowired
	MyRepository repository;
    
    Iterable<MyEntity> findAll(int size){
    	return repository.findAll(PageRequest.of(0, size));
    }
}
