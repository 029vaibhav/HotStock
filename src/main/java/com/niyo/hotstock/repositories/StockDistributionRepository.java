package com.niyo.hotstock.repositories;

import com.niyo.hotstock.models.StockDistribution;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockDistributionRepository extends MongoRepository<StockDistribution, String> {
}
