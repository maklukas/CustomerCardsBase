package com.customercard.customercard.repository;

import com.customercard.customercard.model.Method;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodRepo extends MongoRepository<Method, String> {
}
