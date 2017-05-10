package com.niyo.hotstock.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StockOwnerShip {

    String stockId;
    long qty;
    BigDecimal price;

    @Override
    public String toString() {
        return "StockOwnerShip{" +
                "stockId='" + stockId + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockOwnerShip that = (StockOwnerShip) o;

        return getStockId().equals(that.getStockId());
    }

    @Override
    public int hashCode() {
        return getStockId().hashCode();
    }
}
