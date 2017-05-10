package com.niyo.hotstock.serviceimpl;

import com.niyo.hotstock.exceptions.StockNotDeleted;
import com.niyo.hotstock.exceptions.StockNotFound;
import com.niyo.hotstock.exceptions.StockNotUpdated;
import com.niyo.hotstock.models.Stock;
import com.niyo.hotstock.models.StockDistribution;
import com.niyo.hotstock.models.StockInventory;
import com.niyo.hotstock.models.Transaction;
import com.niyo.hotstock.models.enums.Status;
import com.niyo.hotstock.models.enums.StockStatus;
import com.niyo.hotstock.models.enums.TransactionType;
import com.niyo.hotstock.repositories.StockRepository;
import com.niyo.hotstock.services.StockDistributionService;
import com.niyo.hotstock.services.StockService;
import com.niyo.hotstock.services.UserDetailService;
import com.niyo.hotstock.util.Observable;
import com.niyo.hotstock.util.Observer;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.niyo.hotstock.exceptions.Messages.*;

@Service
@Slf4j
public class StockServiceImpl implements StockService, Observable {

    @Autowired
    StockRepository stockRepository;
    @Autowired
    StockDistributionService stockDistributionService;
    List<Observer> observers = new ArrayList<>();
    @Autowired
    UserDetailService userDetailService;


    @Override
    @Transactional
    public Stock createNewStock(Stock stock) {

        validate(stock);
        if (stock.getStockStatus() == null)
            stock.setStockStatus(StockStatus.INACTIVE);
        stock.getStockInventory().setLastTradedPrice(stock.getBasePrice());
        Stock save = stockRepository.save(stock);
        notifyObserver(save);
        return save;
    }

    private void validate(Stock stock) {
        try {
            Stock stock1 = getStock(stock.getId());
        } catch (StockNotFound e) {
            // stock not found and thus can be proceeded
        }
    }


    @Override
    public Stock updateStock(Stock stock) {
        Stock persistedStock = getStock(stock.getId());
        stock.setStockStatus(persistedStock.getStockStatus());
        stock.setBasePrice(persistedStock.getBasePrice());
        stock.setStockInventory(persistedStock.getStockInventory());
        return stockRepository.save(stock);
    }

    @Override
    public Stock getStock(String stockId) {
        Stock one = stockRepository.findOne(stockId);
        if (one == null) {
            throw new StockNotFound(STOCK_NOT_FOUND);
        }
        return one;
    }

    @Override
    public List<Stock> getStock(StockStatus stockStatus) {
        return stockRepository.findByStockStatus(stockStatus);
    }

    @Override
    public Stock changeStatus(String stockId, StockStatus stockStatus) {
        if (stockStatus != null) {
            Stock stock = getStock(stockId);
            stock.setStockStatus(stockStatus);
            return stock;
        } else {
            log.error("incorrect status change");
            throw new StockNotUpdated(INCORRECT_STATUS);
        }
    }

    @Override
    public Stock deleteStock(String stockId) {

        StockDistribution stockDistribution = stockDistributionService.getById(stockId);
        Stock stock = getStock(stockId);
        if (stockDistribution == null) {
            stockRepository.delete(stockId);
            return stock;
        } else {
            log.error("stock id not found {}", stockId);
            throw new StockNotDeleted(STOCK_DISTRIBUTION_EXISTS);
        }
    }

    @Override
    public Stock updateInventory(String stockId, StockInventory stockInventory) {
        Stock stock = getStock(stockId);
        stock.setStockInventory(stockInventory);
        return stockRepository.save(stock);
    }

    @Override
    public List<Stock> stocksAfterUpdatedTime(DateTime dateTime) {
        return stockRepository.findByStockInventoryLastUpdatedTimeAfter(dateTime);
    }

    @Override
    public void update(Object o) {
        if (o instanceof Transaction) {
            Transaction transaction = (Transaction) o;
            if (transaction.getStatus() == Status.ACCEPTED) {
                updateLastTradedPrice(transaction);
            }
        }
    }

    private void updateLastTradedPrice(Transaction transaction) {
        Stock stock = getStock(transaction.getStockId());

        if (transaction.getTransactionType() == TransactionType.OWNER_TRANSFER) {
            stock.setStockStatus(StockStatus.ACTIVE);
        }
        stock.getStockInventory().setLastTradedPrice(transaction.getBidPrice());
        stock.getStockInventory().setLastUpdatedTime(DateTime.now());
        stockRepository.save(stock);


    }

    @PostConstruct
    @Override
    public void addObserver() {
        observers.add(userDetailService);
        observers.add(stockDistributionService);
    }

    @Override
    public void notifyObserver(Object t) {
        observers.forEach(observer -> observer.update(t));
    }
}
