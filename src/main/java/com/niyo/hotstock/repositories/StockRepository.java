package com.niyo.hotstock.repositories;

import com.niyo.hotstock.models.Stock;
import com.niyo.hotstock.models.enums.StockStatus;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StockRepository extends MongoRepository<Stock, String> {

    List<Stock> findByStockStatus(StockStatus stockStatus);

    List<Stock> findByStockInventoryLastUpdatedTimeAfter(DateTime dateTime);
}

