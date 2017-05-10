package com.niyo.hotstock.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.niyo.hotstock.models.enums.Status;
import com.niyo.hotstock.models.enums.TransactionType;
import com.niyo.hotstock.util.CustomDateTimeDeserializer;
import com.niyo.hotstock.util.CustomDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class Transaction {

    @Id
    String id;
    String buyerId;
    String sellerId;
    @NotNull
    String stockId;
    @NotNull
    BigDecimal bidPrice;
    @NotNull
    TransactionType transactionType;
    @NotNull
    Status status;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    DateTime requestedDate;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    DateTime acceptedDate;
    long qty;

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", stockId='" + stockId + '\'' +
                ", bidPrice=" + bidPrice +
                ", transactionType=" + transactionType +
                ", status=" + status +
                ", requestedDate=" + requestedDate +
                ", acceptedDate=" + acceptedDate +
                ", qty=" + qty +
                '}';
    }
}
