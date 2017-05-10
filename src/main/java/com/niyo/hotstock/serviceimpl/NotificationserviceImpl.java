package com.niyo.hotstock.serviceimpl;

import com.niyo.hotstock.models.Notification;
import com.niyo.hotstock.models.StockDistribution;
import com.niyo.hotstock.models.Transaction;
import com.niyo.hotstock.models.UserDetail;
import com.niyo.hotstock.models.enums.Status;
import com.niyo.hotstock.models.enums.TransactionType;
import com.niyo.hotstock.repositories.NotificationRepository;
import com.niyo.hotstock.services.NotificationService;
import com.niyo.hotstock.services.StockDistributionService;
import com.niyo.hotstock.services.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationserviceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    StockDistributionService stockDistributionService;
    @Autowired
    UserDetailService userDetailService;

    @Override
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification getNotification(String notificationId) {
        return notificationRepository.findOne(notificationId);
    }

    @Override
    public Notification deleteNotification(String notificationId) {
        Notification one = notificationRepository.findOne(notificationId);
        if (one != null)
            notificationRepository.delete(one);
        return one;
    }

    @Override
    public List<Notification> getTransactionByStatusAndUserId(Status status, String userId) {
        return notificationRepository.findByStatusAndNotifiedUserIds(status, userId);
    }


    @Override
    public void update(Object o) {

        if (o instanceof Transaction) {
            Transaction transaction = (Transaction) o;
            if (transaction.getStatus() == Status.INITIATED) {
                if (transaction.getTransactionType() == TransactionType.BUY) {
                    StockDistribution byId = stockDistributionService.getById(transaction.getStockId());
                    if (byId != null) {
                        List<String> collect = byId.getOwnerList().entrySet().stream().map(stringOwnerEntry -> stringOwnerEntry.getValue().getUserId())
                                .collect(Collectors.toList());
                        collect.remove(transaction.getBuyerId());
                        Notification notification = newNotification(transaction, collect);
                        createNotification(notification);
                    }
                } else if (transaction.getTransactionType() == TransactionType.SELL) {
                    List<UserDetail> allUsers = userDetailService.getAllUsers();
                    if (allUsers != null && allUsers.size() > 0) {
                        List<String> collect = allUsers.stream().map(UserDetail::getUserId).collect(Collectors.toList());
                        Notification notification = newNotification(transaction, collect);
                        createNotification(notification);
                    }
                }
            } else if (transaction.getStatus() == Status.ACCEPTED) {
                Notification oneByTransactionId = notificationRepository.findOneByTransactionId(transaction.getId());
                if (oneByTransactionId != null) {
                    oneByTransactionId.setStatus(Status.ACCEPTED);
                    createNotification(oneByTransactionId);
                }
            }
        }

    }

    private Notification newNotification(Transaction transaction, List<String> userIds) {

        Notification notification = new Notification();
        notification.setBidPrice(transaction.getBidPrice());
        if (transaction.getTransactionType() == TransactionType.BUY)
            notification.setInitiatorUserId(transaction.getBuyerId());
        else if (transaction.getTransactionType() == TransactionType.SELL)
            notification.setInitiatorUserId(transaction.getSellerId());
        notification.getNotifiedUserIds().addAll(userIds);
        notification.setStockId(transaction.getStockId());
        notification.setTransactionId(transaction.getId());
        notification.setStatus(transaction.getStatus());
        return notification;
    }
}
