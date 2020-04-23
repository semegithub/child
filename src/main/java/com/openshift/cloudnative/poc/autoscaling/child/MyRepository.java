package com.openshift.cloudnative.poc.autoscaling.child;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface MyRepository extends CrudRepository<MyEntity, Integer> {

    List<MyEntity> findByName(String f1);
    
    MyEntity findByEntityId(int f1);
    
    MyEntity save(MyEntity entity);
    
    void delete(MyEntity entity);
    
    
}
