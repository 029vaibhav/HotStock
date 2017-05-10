package com.niyo.hotstock.controllers;

import com.niyo.hotstock.models.Stock;
import com.niyo.hotstock.models.enums.StockStatus;
import com.niyo.hotstock.services.StockService;
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
@RequestMapping("/api/hotstock/v1/stock")
@Slf4j
public class StockController {

    @Autowired
    StockService stockService;

    @RequestMapping(value = "/create",
            method = RequestMethod.POST)
    public Stock createStock(@RequestBody @Valid Stock stock) {
        log.info("Request for stock creation : {}" + stock.toString());
        return stockService.createNewStock(stock);
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.PUT)
    public Stock updateStock(@RequestBody @Valid Stock stock) {
        log.info("Request for stock update : {}" + stock.toString());
        return stockService.updateStock(stock);
    }

    @RequestMapping(value = "/getall/{status}",
            method = RequestMethod.GET)
    public List<Stock> getAllStocks(@PathVariable("status") String status) {
        log.info("Request for get all stocks ");
        return stockService.getStock(StockStatus.valueOf(status.toUpperCase()));
    }

    @RequestMapping(value = "/status/{stockId}/{status}",
            method = RequestMethod.PUT)
    public Stock changeStatus(@PathVariable("stockId") String stockId, @PathVariable("status") String status) {
        log.info("Request received for status change of stock");
        return stockService.changeStatus(stockId, StockStatus.valueOf(status.toUpperCase()));
    }

    @RequestMapping(value = "/time",
            method = RequestMethod.GET)
    public List<Stock> stockUpdateAfterTime(@RequestParam("time") String time) {
        log.info("Request received for latest change");
        DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_PATTERN);
        DateTime dt = formatter.parseDateTime(time);
        return stockService.stocksAfterUpdatedTime(dt);
    }

}
