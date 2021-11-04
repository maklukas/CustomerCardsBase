package com.customercard.customercard.repository;

import com.customercard.customercard.model.Color;
import com.customercard.customercard.model.Method;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MethodRepo extends MongoRepository<Method, String> {
    List<Color> findByName(String name);
}
