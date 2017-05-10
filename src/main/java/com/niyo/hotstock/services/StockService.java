package com.niyo.hotstock.services;

import com.niyo.hotstock.models.Stock;
import com.niyo.hotstock.models.StockInventory;
import com.niyo.hotstock.models.enums.StockStatus;
import com.niyo.hotstock.util.Observer;
import org.joda.time.DateTime;

import java.util.List;

public interface StockService extends Observer {

    Stock createNewStock(Stock stock);

    Stock updateStock(Stock stock);

    Stock getStock(String stockId);

    List<Stock> getStock(StockStatus stockStatus);

    Stock changeStatus(String stockId, StockStatus stockStatus);

    Stock deleteStock(String stockId);

    Stock updateInventory(String stockId, StockInventory stockInventory);

    List<Stock> stocksAfterUpdatedTime(DateTime dateTime);


}
