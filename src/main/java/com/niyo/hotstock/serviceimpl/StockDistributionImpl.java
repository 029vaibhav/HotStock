package com.niyo.hotstock.serviceimpl;

import com.niyo.hotstock.models.*;
import com.niyo.hotstock.models.enums.Status;
import com.niyo.hotstock.repositories.StockDistributionRepository;
import com.niyo.hotstock.services.StockDistributionService;
import com.niyo.hotstock.services.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class StockDistributionImpl implements StockDistributionService {

    @Autowired
    StockDistributionRepository repository;
    @Autowired
    UserDetailService userDetailService;

    @Override
    public StockDistribution save(StockDistribution stockDistribution) {

        return repository.save(stockDistribution);
    }


    @Override
    public StockDistribution getById(String stockId) {
        return repository.findOne(stockId);
    }

    @Override
    public StockDistribution delete(String stockId) {
        StockDistribution byId = getById(stockId);
        if (byId != null)
            repository.delete(stockId);
        return byId;
    }

    @Override
    public void updateStockOwner(String stockId, Owner owner) {
        StockDistribution byId = getById(stockId);
        if (byId == null) {
            if (owner.getQty() > 0)
                save(generateStockDistribution(stockId, owner));
        } else {
            if (owner.getQty() == 0) {
                byId.getOwnerList().remove(owner.getUserId());
            } else
                byId.getOwnerList().put(owner.getUserId(), owner);
            save(byId);
        }
    }

    @Override
    public Owner getStockIdAndOwnerId(String stockId, String ownerId) {

        StockDistribution byId = getById(stockId);
        if (byId != null) {
            Owner owner = byId.getOwnerList().get(ownerId);
            return owner;
        }
        return null;
    }

    private StockDistribution generateStockDistribution(String stockId, Owner owner) {

        HashMap<String, Owner> ownerList = new HashMap<>();
        ownerList.put(owner.getUserId(), owner);
        return StockDistribution.builder().stockId(stockId).ownerList(ownerList).build();
    }

    @Override
    public void update(Object o) {

        if (o instanceof Transaction) {
            Transaction transaction = (Transaction) o;
            if (transaction.getStatus() == Status.ACCEPTED) {
                transferOwnerShip(transaction);
            }
        } else if (o instanceof Stock) {
            Stock save = (Stock) o;
            updateStockOwner(save.getId(), createOwner(save.getStockInventory().getTotalQty(), save.getCreator()));

        }


    }

    private Owner createOwner(long totalQty, String creator) {
        return Owner.builder().userId(creator).qty(totalQty).build();
    }

    private void transferOwnerShip(Transaction transaction) {

        StockDistribution byId = getById(transaction.getStockId());
        if (byId != null) {
            Owner oldOwner = byId.getOwnerList().get(transaction.getSellerId());
            if (oldOwner != null) {
                oldOwner.setQty(oldOwner.getQty() - transaction.getQty());
                Owner newOwner = byId.getOwnerList().get(transaction.getBuyerId());
                if (newOwner == null) {
                    newOwner = Owner.builder().userId(transaction.getBuyerId()).qty(transaction.getQty()).build();
                } else newOwner.setQty(newOwner.getQty() + transaction.getQty());
                UserDetail oldUserDetail = getUserDetail(oldOwner.getUserId());
                if (oldOwner.getQty() == 0) {
                    byId.getOwnerList().remove(oldOwner.getUserId());
                    removeUserOwnerShip(oldUserDetail, transaction);
                } else {
                    byId.getOwnerList().put(oldOwner.getUserId(), oldOwner);
                    updateUserOwnerShip(oldUserDetail, transaction, oldOwner.getQty());
                }
                userDetailService.update(oldUserDetail);
                byId.getOwnerList().put(newOwner.getUserId(), newOwner);
                addNewUserStockDetail(newOwner, transaction);
                save(byId);
            }
        }
    }


    private void addNewUserStockDetail(Owner owner, Transaction transaction) {
        UserDetail newUser = getUserDetail(owner.getUserId());
        newUser.getStockOwnerShipList().add(createStockOwnerShip(transaction.getQty(), transaction.getBidPrice(), transaction.getStockId()));
        userDetailService.update(newUser);
    }

    private void updateUserOwnerShip(UserDetail oldUserDetail, Transaction transaction, long qty) {

        for (int i = 0; i < oldUserDetail.getStockOwnerShipList().size(); i++) {
            if (oldUserDetail.getStockOwnerShipList().get(i).getStockId().equals(transaction.getStockId())) {
                oldUserDetail.getStockOwnerShipList().get(i).setQty(qty);
                break;
            }
        }
    }

    private void removeUserOwnerShip(UserDetail oldUserDetail, Transaction transaction) {

        if (oldUserDetail != null) {
            for (int i = oldUserDetail.getStockOwnerShipList().size() - 1; i >= 0; i--) {
                if (oldUserDetail.getStockOwnerShipList().get(i).getStockId().equals(transaction.getStockId())) {
                    oldUserDetail.getStockOwnerShipList().remove(i);
                    break;
                }

            }
        }
    }


    private UserDetail getUserDetail(String userId) {
        return userDetailService.getUserDetail(userId);
    }

    private StockOwnerShip createStockOwnerShip(long qty, BigDecimal price, String stockId) {

        StockOwnerShip stockOwnerShip = new StockOwnerShip();
        stockOwnerShip.setQty(qty);
        stockOwnerShip.setPrice(price);
        stockOwnerShip.setStockId(stockId);
        return stockOwnerShip;
    }
}
