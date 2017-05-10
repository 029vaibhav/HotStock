package com.niyo.hotstock.models;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.HashMap;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDistribution {

    @Id
    String stockId;
    HashMap<String,Owner> ownerList = new HashMap<>();
}
