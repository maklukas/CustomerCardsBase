package com.customercard.customercard.repository;

import com.customercard.customercard.model.Style;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleRepo extends MongoRepository<Style, String> {
    List<Style> findByName(String name);
}
