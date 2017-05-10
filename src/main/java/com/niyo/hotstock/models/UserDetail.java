package com.niyo.hotstock.models;

import com.niyo.hotstock.models.enums.UserType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDetail {

    @Id
    String userId;
    @NotNull(message = "name cant be empty")
    @NotEmpty
    String name;
    @NotNull
    UserType role;
    List<StockOwnerShip> stockOwnerShipList = new ArrayList<>();


    @Override
    public String toString() {
        return "UserDetail{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", stockOwnerShipList=" + stockOwnerShipList +
                '}';
    }
}
