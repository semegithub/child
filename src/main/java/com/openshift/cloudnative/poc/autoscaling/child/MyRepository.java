package com.openshift.cloudnative.poc.autoscaling.child;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface MyRepository extends CrudRepository<MyEntity, Integer> {

//    List<MyEntity> findByF1(String f1);
    
    Optional<MyEntity> findById(int id);
    Iterable<MyEntity> findAll();
    
    @SuppressWarnings("unchecked")
	MyEntity save(MyEntity entity);
    
    void delete(MyEntity entity);
}
