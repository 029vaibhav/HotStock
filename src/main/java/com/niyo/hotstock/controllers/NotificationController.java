package com.niyo.hotstock.controllers;

import com.niyo.hotstock.models.Notification;
import com.niyo.hotstock.models.enums.Status;
import com.niyo.hotstock.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hotstock/v1/notification")
@Slf4j
public class NotificationController {


    @Autowired
    NotificationService notificationService;

    @RequestMapping(value = "view/{userId}/{status}",
            method = RequestMethod.GET)
    public List<Notification> getTransactionByStatus(@PathVariable("status") String status, @PathVariable("userId") String userId) {
        log.info("Request for transaction view with status {}", status);
        return notificationService.getTransactionByStatusAndUserId(Status.valueOf(status), userId);
    }


}
