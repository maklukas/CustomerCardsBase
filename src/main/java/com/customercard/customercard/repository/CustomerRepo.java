package com.customercard.customercard.repository;

import com.customercard.customercard.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerRepo extends MongoRepository<Customer, String> {

    @Query(
            value="{ 'lashesList': { $elemMatch: { 'nextDate': {$gte: ?0, $lt: ?1 } } } }",
            fields="{ 'name': 1, 'surname': 1, 'lashesList': { $elemMatch: { 'nextDate': {$gte: ?0, $lt: ?1 } } } }"
    )
    List<Customer> findAllBetweenDates(LocalDateTime gte, LocalDateTime lt);
}
