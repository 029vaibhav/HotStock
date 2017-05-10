package com.niyo.hotstock.controllers;

import com.niyo.hotstock.models.Transaction;
import com.niyo.hotstock.models.enums.Status;
import com.niyo.hotstock.services.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.niyo.hotstock.util.Constants.DATE_PATTERN;

@RestController
@RequestMapping("/api/hotstock/v1/transaction")
@Slf4j
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/create",
            method = RequestMethod.POST)
    public Transaction registerUser(@RequestBody @Valid Transaction transaction) {
        log.info("Request for transaction creation : {}" + transaction.toString());
        return transactionService.save(transaction);
    }

    @RequestMapping(value = "/accept/sell/{transactionId}/{sellerId}",
            method = RequestMethod.POST)
    public Transaction acceptSell(@PathVariable("transactionId") String transactionId, @PathVariable("sellerId") String sellerId) {
        log.info("Request for acceptance of transaction {}", transactionId);
        return transactionService.acceptSell(transactionId, sellerId);
    }

    @RequestMapping(value = "/accept/buy/{transactionId}/{buyerId}",
            method = RequestMethod.POST)
    public Transaction acceptBuy(@PathVariable("transactionId") String transactionId, @PathVariable("sellerId") String sellerId) {
        log.info("Request for acceptance of transaction {}", transactionId);
        return transactionService.acceptSell(transactionId, sellerId);
    }

    @RequestMapping(value = "view/{userId}/{status}",
            method = RequestMethod.POST)
    public List<Transaction> getTransactionByStatus(@PathVariable("status") String status, @PathVariable("userId") String userId) {
        log.info("Request for transaction view with status {}", status);
        return transactionService.getTransactionByStatus(Status.valueOf(status), userId);
    }

    @RequestMapping(value = "/report",
            method = RequestMethod.GET)
    public List<Transaction> stockUpdateAfterTime(@RequestParam("start") String startTime, @RequestParam("end") String endTime) {
        log.info("Request received for latest change");
        DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_PATTERN);
        DateTime startDateTime = formatter.parseDateTime(startTime);
        DateTime endDateTime = formatter.parseDateTime(endTime);
        return transactionService.getByAcceptedDate(startDateTime, endDateTime);
    }


}
