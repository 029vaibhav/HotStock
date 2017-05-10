package com.niyo.hotstock.repositories;

import com.niyo.hotstock.models.UserDetail;
import com.niyo.hotstock.models.enums.UserType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<UserDetail, String> {

    @Query(value = "{'role':?0}",fields = "{'user_id':1}")
    List<UserDetail> findAllUserIds(UserType role);
}
