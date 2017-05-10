package com.niyo.hotstock.serviceimpl;

import com.niyo.hotstock.exceptions.TransactionFailed;
import com.niyo.hotstock.models.Owner;
import com.niyo.hotstock.models.Transaction;
import com.niyo.hotstock.models.enums.Status;
import com.niyo.hotstock.models.enums.TransactionType;
import com.niyo.hotstock.repositories.TransactionRepository;
import com.niyo.hotstock.services.NotificationService;
import com.niyo.hotstock.services.StockDistributionService;
import com.niyo.hotstock.services.StockService;
import com.niyo.hotstock.services.TransactionService;
import com.niyo.hotstock.util.Observable;
import com.niyo.hotstock.util.Observer;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.niyo.hotstock.exceptions.Messages.*;

@Service
public class TransactionServiceImpl implements TransactionService, Observable {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    NotificationService notificationService;
    List<Observer> observers = new ArrayList<>();
    @Autowired
    StockDistributionService stockDistributionService;
    @Autowired
    StockService stockService;


    @Override
    @Transactional
    public Transaction save(Transaction transaction) {

        transactionDataValidator(transaction);

        if (transaction.getTransactionType() == TransactionType.OWNER_TRANSFER) {
            transaction.setStatus(Status.ACCEPTED);
        }
        if (transaction.getStatus() == Status.INITIATED) {
            transaction.setSellerId(null);
            transaction.setRequestedDate(DateTime.now());
        }
        if (transaction.getStatus() == Status.ACCEPTED) {
            transaction.setAcceptedDate(DateTime.now());
        }
        Transaction save = transactionRepository.save(transaction);
        notifyObserver(save);
        return save;
    }

    private void transactionDataValidator(Transaction transaction) {
        if (transaction.getTransactionType() == TransactionType.BUY) {
            if (StringUtils.isEmpty(transaction.getBuyerId())) {
                throw new TransactionFailed(BUYER_EMPTY);
            }
        } else if (transaction.getTransactionType() == TransactionType.SELL) {
            if (StringUtils.isEmpty(transaction.getSellerId())) {
                throw new TransactionFailed(SELLER_EMPTY);
            }
        }
        if (transaction.getBidPrice() == null) {
            throw new TransactionFailed(BID_PRICE);
        }

    }

    @Override
    public Transaction acceptSell(String transactionId, String sellerId) {

        Transaction transaction = get(transactionId);
        if (transaction != null) {
            synchronized (transaction.getId()) {
                if (transaction.getStatus() == Status.ACCEPTED) {
                    throw new TransactionFailed(TRANSACTION_PROCESSED);
                }
                if (transaction.getBuyerId().equals(sellerId)) {
                    throw new TransactionFailed(SELLER_BUYER_SAME);
                }
                boolean available = isQuantityAvailableWithSeller(transaction.getStockId(), sellerId);
                if (!available) {
                    throw new TransactionFailed(QUANTITY_NOT_AVAILABLE);
                }
                transaction.setSellerId(sellerId);
                transaction.setStatus(Status.ACCEPTED);
            }
            return save(transaction);
        } else {
            throw new TransactionFailed(TRANSACTION_ID_NOT_FOUND);
        }
    }

    @Override
    public Transaction acceptBuy(String transactionId, String buyerId) {
        Transaction transaction = get(transactionId);
        if (transaction != null) {
            synchronized (transaction.getId()) {
                if (transaction.getStatus() == Status.ACCEPTED) {
                    throw new TransactionFailed(TRANSACTION_PROCESSED);
                }
                if (transaction.getSellerId().equals(buyerId)) {
                    throw new TransactionFailed(SELLER_BUYER_SAME);
                }
                transaction.setBuyerId(buyerId);
                transaction.setStatus(Status.ACCEPTED);
            }
            return save(transaction);
        } else {
            throw new TransactionFailed(TRANSACTION_ID_NOT_FOUND);
        }
    }

    private boolean isQuantityAvailableWithSeller(String stockId, String sellerId) {
        Owner stockIdAndOwnerId = stockDistributionService.getStockIdAndOwnerId(stockId, sellerId);
        if (stockIdAndOwnerId != null && stockIdAndOwnerId.getQty() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Transaction get(String transactionId) {
        return transactionRepository.findOne(transactionId);
    }

    @Override
    public Transaction delete(String transactionId) {
        Transaction transaction = get(transactionId);
        if (transaction != null)
            transactionRepository.delete(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> getByAcceptedDate(DateTime dateTime, DateTime dateTime2) {
        return transactionRepository.findByAcceptedDateBetween(dateTime, dateTime2);
    }

    @Override
    public List<Transaction> getByRequestedDate(DateTime dateTime, DateTime dateTime2) {
        return transactionRepository.findByRequestedDateBetween(dateTime, dateTime2);
    }

    @Override
    public Iterable<Transaction> get(List<String> transactionId) {
        return transactionRepository.findAll(transactionId);
    }

    @Override
    public List<Transaction> getTransactionByStatus(Status status, String notifiedUserId) {
        return null;
    }

    @Override
    @PostConstruct
    public void addObserver() {
        observers.add(notificationService);
        observers.add(stockDistributionService);
        observers.add(stockService);
    }

    @Override
    public void notifyObserver(Object o) {
        for (Observer observer : observers) {
            observer.update(o);
        }
    }
}
