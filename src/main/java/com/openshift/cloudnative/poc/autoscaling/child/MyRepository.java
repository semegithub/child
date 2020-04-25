package com.openshift.cloudnative.poc.autoscaling.child;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyRepository extends JpaRepository<MyEntity, Integer> {

//    List<MyEntity> findByF1(String f1);
    
    Optional<MyEntity> findById(int id);
    List<MyEntity> findAll();
    
    Iterable<MyEntity> findAll(PageRequest pageRequest);
    
    @SuppressWarnings("unchecked")
	MyEntity save(MyEntity entity);
    
    void delete(MyEntity entity);
}
