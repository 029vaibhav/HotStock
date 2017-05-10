package com.niyo.hotstock.services;

import com.niyo.hotstock.models.Owner;
import com.niyo.hotstock.models.StockDistribution;
import com.niyo.hotstock.util.Observer;

public interface StockDistributionService extends Observer {

    StockDistribution save(StockDistribution stockDistribution);

    StockDistribution getById(String stockId);

    StockDistribution delete(String stockId);

    void updateStockOwner(String stockId, Owner owner);

    Owner getStockIdAndOwnerId(String stockId,String ownerId);


}