package com.niyo.hotstock.repositories;

import com.niyo.hotstock.models.Transaction;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {


    List<Transaction> findByAcceptedDateBetween(DateTime dateTime1, DateTime dateTime2);

    List<Transaction> findByRequestedDateBetween(DateTime dateTime1, DateTime dateTime2);
}
