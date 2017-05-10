package com.niyo.hotstock.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.niyo.hotstock.util.CustomDateTimeDeserializer;
import com.niyo.hotstock.util.CustomDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.math.BigDecimal;

@Getter
@Setter
public class StockInventory {

    long totalQty;
    BigDecimal lastTradedPrice;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    DateTime lastUpdatedTime;

    @Override
    public String toString() {
        return "StockInventory{" +
                "totalQty=" + totalQty +
                ", lastTradedPrice=" + lastTradedPrice +
                ", lastUpdatedTime=" + lastUpdatedTime +
                '}';
    }
}
