package com.openshift.cloudnative.poc.autoscaling.child;

import java.awt.print.Book;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface MyRepository extends CrudRepository<Book, Long> {

    List<MyEntity> findByName(String name);

}
