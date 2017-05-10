package com.niyo.hotstock.services;

import com.niyo.hotstock.models.UserDetail;
import com.niyo.hotstock.models.enums.UserType;
import com.niyo.hotstock.util.Observer;

import java.util.List;

public interface UserDetailService extends Observer{

    UserDetail registerUser(UserDetail userDetail);

    UserDetail getUserDetail(String userId);

    UserDetail delete(String userId);

    UserDetail update(UserDetail userDetail);

    List<UserDetail> getAllUsers();

    List<String> getAllUserIds(UserType userType);

}
