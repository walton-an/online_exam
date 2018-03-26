package com.online.examing.repository;

import com.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: walton
 * @Description: 登录、注册的Repository
 * @Createtime: 2017/11/28
 */

@Repository
public interface UserRepository extends MongoRepository<User,Long> {

//    public User findById(Long id);

    public User findByAccountNumber(Long accountNumber);

    public User findByAccountNumberAndPassword(Long accountNumber, String password);
}
