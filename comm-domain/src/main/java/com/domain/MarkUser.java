package com.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: walton
 * @Description: 评卷人
 * @Createtime: 2018/4/28
 */
@Document(collection="markUser")
public class MarkUser{
    private String userName;
    private int score;
    private long userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserIdStr() {
        return this.userId + "";
    }


    public void setUserId(long userId) {
        this.userId = userId;
    }
}
