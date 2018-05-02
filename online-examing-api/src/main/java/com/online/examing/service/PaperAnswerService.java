package com.online.examing.service;

import com.domain.ExamPaper;
import com.domain.MarkUser;
import com.domain.PaperAnswer;
import com.mongodb.util.JSON;
import com.online.examing.domain.PaperRequestDto;
import com.online.examing.domain.UserRequestDto;
import com.online.examing.repository.ExamRepository;
import com.online.examing.repository.PaperAnswerRepository;
import com.online.examing.repository.UserRepository;
import com.utils.DefaultKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author: walton
 * @Description: 登录、注册的Service
 * @Createtime: 2017/11/28
 */

@Service
public class PaperAnswerService {

    @Autowired
    private PaperAnswerRepository paperAnswerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private DefaultKeyGenerator defaultKeyGenerator ;

    //正则匹配是否为数字
    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
    //保留两位小数
    DecimalFormat df = new DecimalFormat("0.0");

    /**
     *@Description: 插入题目
     *@Date: 2017/11/28
     */
    public PaperAnswer addPaperAnswer(PaperAnswer paperAnswer){
//        double score = 0;       //用户得分
//        double totalScore = 0;  //试卷满分
        if(paperAnswer.getId()==0)
            paperAnswer.setId((Long) defaultKeyGenerator.generateKey());
        if(paperAnswer.getCreateTime()==0) {
            long now = System.currentTimeMillis();
            paperAnswer.setCreateTime(now);
        }
        paperAnswer.setMarkUsers(null);
//        ExamPaper examPaper = examRepository.findById(paperAnswer.getPaperId());
//        List<PaperQuestion> questions = examPaper.getExamQuestion();
//        for (int i = 0; i < questions.size(); i++) {
//            try {
//                totalScore += questions.get(i).getScore();
//                if (questions.get(i).getType() == 0) {
//                    if (paperAnswer.getSelectAnswers().get(i).equals(questions.get(i).getChoiceAnswer().get(0))) {
//                        score += questions.get(i).getScore();
//                    }
//                } else if (questions.get(i).getType() == 1) {
//                    if (paperAnswer.getTfAnswers().get(i).equals(questions.get(i).getTfAnswer())) {
//                        score += questions.get(i).getScore();
//                    }
//                } else if (questions.get(i).getType() == 2) {
//                    //如果主观题答案为数字的话，则全匹配，否则计算花名
//                    if(isInteger(questions.get(i).getBlankAnswer())){
//                        if(paperAnswer.getFullAnswer().get(i).equals(questions.get(i).getBlankAnswer()))
//                            score += questions.get(i).getScore();
//                    } else {
//                        String rightAnswer = questions.get(i).getBlankAnswer();
//                        String userAnswer = paperAnswer.getFullAnswer().get(i);
//                        MySimHash mySimHash1 = new MySimHash(rightAnswer,64);
//                        MySimHash mySimHash2 = new MySimHash(userAnswer, 64);
//                        double similar = mySimHash1.getSemblance(mySimHash2);
//                        score += questions.get(i).getScore() * similar;
//                    }
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        paperAnswer.setScore(Double.parseDouble(df.format(score*100/totalScore)));
        return paperAnswerRepository.save(paperAnswer);
    }

    public boolean isInteger(String str) {
        return pattern.matcher(str).matches();
    }

    public Map getPaperAnswer(PaperRequestDto paperRequestDto){
        Query query = new Query();
        int skip = (paperRequestDto.getPage()-1)*paperRequestDto.getPageSize();
        query.skip(skip).limit(paperRequestDto.getPageSize());
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"score")));
        if(paperRequestDto.getTeacherId() != 0){
            query.addCriteria(Criteria.where("teacherId").is(paperRequestDto.getTeacherId()));
        }
        if(paperRequestDto.getPaperId() != 0){
            query.addCriteria(Criteria.where("paperId").is(paperRequestDto.getPaperId()));
        }
        if(paperRequestDto.getExamClassStr() != null) {
            List<String> examClass = (List<String>) JSON.parse(paperRequestDto.getExamClassStr());
            query.addCriteria(Criteria.where("examClass").in(examClass));
        }
        List<PaperAnswer> paperAnswerList = mongoTemplate.find(query, PaperAnswer.class);

//        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "score");
//        Sort sort = new Sort(order);
//        Pageable pageable = new PageRequest(paperRequestDto.getPage()-1, paperRequestDto.getPageSize(), sort);//对数据进行分页
        Map map = new HashMap<>();
        map.put("list", paperAnswerList);
        map.put("size", mongoTemplate.count(query, PaperAnswer.class));
        return map;
    }


    public ExamPaper getPaperAnswerDetail(long id){
        return examRepository.findOne(id);
    }

    public Map getGroupPaperAnswer(PaperRequestDto paperRequestDto){
        Query query = new Query();
        query.addCriteria(Criteria.where("group").is(paperRequestDto.getGroup()));
        query.addCriteria(Criteria.where("managerClass").regex(paperRequestDto.getManagerClass()));
        Integer skip = (paperRequestDto.getPage()-1) * paperRequestDto.getPageSize();
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"createTime")));
        query.skip(skip).limit(paperRequestDto.getPageSize());
        List<PaperAnswer> paperAnswers = mongoTemplate.find(query, PaperAnswer.class);
        Iterator<PaperAnswer> iterator = paperAnswers.iterator();
        while (iterator.hasNext()){
            //剔除本人的答卷
            String id = iterator.next().getStuId() + "";
            if(id.equals(paperRequestDto.getStuId())) {
                iterator.remove();
            }

        }
        Map map = new HashMap<>();
        map.put("list", paperAnswers);
        map.put("size", mongoTemplate.count(query, PaperAnswer.class)-1);
        return map;
    }

    public void delete(UserRequestDto userRequestDto){
        for(long id : userRequestDto.getIds())
            paperAnswerRepository.delete(id);
    }

    public void addPaperScore(PaperRequestDto paperRequestDto){
        PaperAnswer paperAnswer = paperAnswerRepository.findByStuIdAndPaperId(Long.valueOf(paperRequestDto.getStuId()), paperRequestDto.getPaperId());
        MarkUser markUser = new MarkUser();
        markUser.setUserId(paperRequestDto.getMarkUserId());
        markUser.setUserName(paperRequestDto.getUserName());
        markUser.setScore(paperRequestDto.getScore());
        List<MarkUser> markUserList = paperAnswer.getMarkUsers();
        if(markUserList == null) {
            markUserList = new ArrayList<>();
            markUserList.add(markUser);
        }else {
            //若为同一个人评分，则更新比分
            for(MarkUser mark : markUserList){
                if(mark.getUserId() == paperRequestDto.getMarkUserId()){
                    mark.setScore(paperRequestDto.getScore());
                    markUserList.remove(mark);
                    break;
                }
            }
            //否则，添加一个评分人
            markUserList.add(markUser);
        }
        paperAnswer.setMarkUsers(markUserList);
        double score = 0;
        for(MarkUser mark : markUserList){
            score += mark.getScore();
        }
        score = score / markUserList.size();
        paperAnswer.setScore(Double.parseDouble(df.format(score)));
        paperAnswerRepository.save(paperAnswer);
    }
}
