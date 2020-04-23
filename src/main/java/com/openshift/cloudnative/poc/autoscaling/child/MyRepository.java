package com.openshift.cloudnative.poc.autoscaling.child;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface MyRepository extends CrudRepository<MyEntity, Integer> {

//    List<MyEntity> findByF1(String f1);
    
    MyEntity findOne(int id);
    
    MyEntity save(MyEntity entity);
    
    void delete(MyEntity entity);
    
    
}
