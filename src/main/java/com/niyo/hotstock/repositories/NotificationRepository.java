package com.niyo.hotstock.repositories;

import com.niyo.hotstock.models.Notification;
import com.niyo.hotstock.models.enums.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Notification findOneByTransactionId(String transactionId);

    List<Notification> findByStatusAndNotifiedUserIds(Status status, String userId);


}
