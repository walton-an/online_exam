package com.online.examing.repository;

import com.domain.PaperAnswer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * @Author: walton
 * @Description: 登录、注册的Repository
 * @Createtime: 2017/11/28
 */

@Repository
public interface PaperAnswerRepository extends MongoRepository<PaperAnswer,Long> {

    public PaperAnswer findByStuIdAndPaperId(Long stuId, Long paperId);

}
