package com.openshift.cloudnative.poc.autoscaling.child;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface MyRepository extends CrudRepository<MyEntity, String> {

    List<MyEntity> findByName(String name);

}
