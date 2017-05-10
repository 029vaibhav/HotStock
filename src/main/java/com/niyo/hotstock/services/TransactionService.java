package com.niyo.hotstock.services;

import com.niyo.hotstock.models.Transaction;
import com.niyo.hotstock.models.enums.Status;
import org.joda.time.DateTime;

import java.util.List;

public interface TransactionService {

    Transaction save(Transaction transaction);

    Transaction acceptSell(String transactionId, String sellerId);

    Transaction acceptBuy(String transactionId, String buyerId);

    Transaction get(String transactionId);

    Transaction delete(String transactionId);

    List<Transaction> getByAcceptedDate(DateTime dateTime1, DateTime dateTime2);

    List<Transaction> getByRequestedDate(DateTime dateTime1, DateTime dateTime2);

    Iterable<Transaction> get(List<String> transactionId);

    List<Transaction> getTransactionByStatus(Status status,String notifiedUserId);


}
