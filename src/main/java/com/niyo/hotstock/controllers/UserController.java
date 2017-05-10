package com.niyo.hotstock.controllers;

import com.niyo.hotstock.models.UserDetail;
import com.niyo.hotstock.models.enums.UserType;
import com.niyo.hotstock.services.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hotstock/v1/user")
@Slf4j
public class UserController {

    @Autowired
    UserDetailService userDetailService;

    @RequestMapping(value = "/register",
            method = RequestMethod.POST)
    public UserDetail registerUser(@RequestBody @Valid UserDetail userDetail) {
        log.info("Request for user registration : {}" + userDetail.toString());
        return userDetailService.registerUser(userDetail);
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.PUT)
    public UserDetail updateUser(@RequestBody @Valid UserDetail userDetail) {
        log.info("Request for user registration : {}" + userDetail.toString());
        return userDetailService.update(userDetail);
    }

    @RequestMapping(value = "/login/{userId}",
            method = RequestMethod.GET)
    public UserDetail getUserDetail(@PathVariable("userId") String userId) {
        log.info("Request for login {}", userId);
        UserDetail userDetail = userDetailService.getUserDetail(userId);
        log.info("Response for login {}",userDetail.toString());
        return userDetail;
    }

    @RequestMapping(value = "/user-ids/{role}",
            method = RequestMethod.GET)
    public List<String> getUserIds(@PathVariable("role") String role) {
        log.info("Request for user ids with role {}",role);
        return userDetailService.getAllUserIds(UserType.valueOf(role));
    }
}
