package com.online.examing.repository;

import com.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: walton
 * @Description: 登录、注册的Repository
 * @Createtime: 2017/11/28
 */

@Repository
public interface UserRepository extends MongoRepository<User,Long> {

    public List<User> findByAccountNumberExists(Long accountNumber);

    public List<User> findByNameContains(String name, Pageable pageable);

    public User findByAccountNumber(Long accountNumber);

    public User findByAccountNumberAndPasswordAndStatus(Long accountNumber, String password,int status);

    public User findByAccountNumberAndStatus(Long accountNumber, int status);
}
