package com.niyo.hotstock.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.niyo.hotstock.models.enums.StockStatus;
import com.niyo.hotstock.util.CustomDateTimeDeserializer;
import com.niyo.hotstock.util.CustomDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class Stock {

    @Id
    String id;
    @NotNull
    String name;
    @NotNull
    String symbol;
    BigDecimal basePrice;
    StockInventory stockInventory;
    @CreatedDate
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    DateTime publishDate;
    StockStatus stockStatus;
    String creator;



    @Override
    public String toString() {
        return "Stock{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", basePrice=" + basePrice +
                ", stockInventory=" + stockInventory +
                ", publishDate=" + publishDate +
                ", stockStatus=" + stockStatus +
                '}';
    }
}
