package com.niyo.hotstock.serviceimpl;

import com.niyo.hotstock.exceptions.Messages;
import com.niyo.hotstock.exceptions.UserNotFound;
import com.niyo.hotstock.exceptions.UserRegistrationFailed;
import com.niyo.hotstock.models.Stock;
import com.niyo.hotstock.models.StockOwnerShip;
import com.niyo.hotstock.models.UserDetail;
import com.niyo.hotstock.models.enums.UserType;
import com.niyo.hotstock.repositories.UserRepository;
import com.niyo.hotstock.services.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.niyo.hotstock.exceptions.Messages.USER_ALREDY_EXISTS;

@Service
public class UserDetailServiceImpl implements UserDetailService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetail registerUser(UserDetail userDetail) {

        UserDetail one = userRepository.findOne(userDetail.getUserId());
        if (one != null) {
            throw new UserRegistrationFailed(USER_ALREDY_EXISTS);
        }
        return userRepository.save(userDetail);
    }

    @Override
    public UserDetail getUserDetail(String userId) {
        UserDetail one = userRepository.findOne(userId);
        if (one == null)
            throw new UserNotFound(Messages.USER_ID_DOESNT_EXISTS);
        return one;
    }

    @Override
    public UserDetail delete(String userId) {
        UserDetail userDetail = getUserDetail(userId);
        if (userDetail != null) {
            userRepository.delete(userId);
        }
        return userDetail;
    }

    @Override
    public UserDetail update(UserDetail userDetail) {

        return userRepository.save(userDetail);
    }

    @Override
    public List<UserDetail> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<String> getAllUserIds(UserType userType) {
        List<UserDetail> allUserIds = userRepository.findAllUserIds(userType);
        return allUserIds.stream().map(user -> user.getUserId()).collect(Collectors.toList());
    }

    @Override
    public void update(Object o) {
        if (o instanceof Stock) {
            Stock stock = (Stock) o;
            UserDetail userDetail = getUserDetail(stock.getCreator());
            StockOwnerShip stockOwnerShip = new StockOwnerShip();
            stockOwnerShip.setStockId(stock.getId());
            stockOwnerShip.setPrice(stock.getBasePrice());
            stockOwnerShip.setQty(stock.getStockInventory().getTotalQty());
            userDetail.getStockOwnerShipList().add(stockOwnerShip);
            update(userDetail);
        }
    }
}
