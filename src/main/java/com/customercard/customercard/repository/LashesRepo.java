package com.customercard.customercard.repository;

import com.customercard.customercard.model.Lashes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LashesRepo extends MongoRepository<Lashes, String> {
}
