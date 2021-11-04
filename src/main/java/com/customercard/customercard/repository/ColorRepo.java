package com.customercard.customercard.repository;

import com.customercard.customercard.model.Color;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepo extends MongoRepository<Color, String> {
}
