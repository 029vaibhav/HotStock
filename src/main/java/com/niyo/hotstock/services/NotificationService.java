package com.niyo.hotstock.services;

import com.niyo.hotstock.models.Notification;
import com.niyo.hotstock.models.enums.Status;
import com.niyo.hotstock.util.Observer;

import java.util.List;

public interface NotificationService extends Observer {

    Notification createNotification(Notification stock);


    Notification getNotification(String stockId);

    Notification deleteNotification(String stockId);

    List<Notification> getTransactionByStatusAndUserId(Status status, String userId);

}
