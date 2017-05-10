package com.niyo.hotstock.models;

import com.niyo.hotstock.models.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Notification {

    @Id
    String id;
    @NotNull
    String stockId;
    @NotNull
    String initiatorUserId;
    List<String> notifiedUserIds = new ArrayList<>();
    @NotNull
    BigDecimal bidPrice;
    @Indexed(unique = true)
    String transactionId;
    Status status;

}
