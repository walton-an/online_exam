package com.online.examing.service;

import com.domain.ExamPaper;
import com.domain.PaperAnswer;
import com.domain.PaperQuestion;
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
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        System.out.println("aaa");
        double score = 0;       //用户得分
        double totalScore = 0;  //试卷满分
        if(paperAnswer.getId()==0)
            paperAnswer.setId((Long) defaultKeyGenerator.generateKey());
        if(paperAnswer.getCreateTime()==0) {
            long now = System.currentTimeMillis();
            paperAnswer.setCreateTime(now);
        }
        ExamPaper examPaper = examRepository.findById(paperAnswer.getPaperId());
        List<PaperQuestion> questions = examPaper.getExamQuestion();
        for (int i = 0; i < questions.size(); i++) {
            try {
                totalScore += questions.get(i).getScore();
                if (questions.get(i).getType() == 0) {
                    if (paperAnswer.getSelectAnswers().get(i).equals(questions.get(i).getChoiceAnswer().get(0))) {
                        score += questions.get(i).getScore();
                    }
                } else if (questions.get(i).getType() == 1) {
                    if (paperAnswer.getTfAnswers().get(i).equals(questions.get(i).getTfAnswer())) {
                        score += questions.get(i).getScore();
                    }
                } else if (questions.get(i).getType() == 2) {
                    //如果主观题答案为数字的话，则全匹配，否则计算花名
                    if(isInteger(questions.get(i).getBlankAnswer())){
                        if(paperAnswer.getFullAnswer().get(i).equals(questions.get(i).getBlankAnswer()))
                            score += questions.get(i).getScore();
                    } else {
                        String rightAnswer = questions.get(i).getBlankAnswer();
                        String userAnswer = paperAnswer.getFullAnswer().get(i);
                        MySimHash mySimHash1 = new MySimHash(rightAnswer,64);
                        MySimHash mySimHash2 = new MySimHash(userAnswer, 64);
                        double similar = mySimHash1.getSemblance(mySimHash2);
                        score += questions.get(i).getScore() * similar;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        paperAnswer.setScore(Double.parseDouble(df.format(score*100/totalScore)));
        return paperAnswerRepository.save(paperAnswer);
    }

    public boolean isInteger(String str) {
        return pattern.matcher(str).matches();
    }

    public Map getPaperAnswer(PaperRequestDto paperRequestDto){
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createTime");
        Sort sort = new Sort(order);
        Pageable pageable = new PageRequest(paperRequestDto.getPage()-1, paperRequestDto.getPageSize(), sort);//对数据进行分页
        Map map = new HashMap<>();
        map.put("list", paperAnswerRepository.findAll(pageable).getContent());
        map.put("size", paperAnswerRepository.count());
        return map;
    }

    public void delete(UserRequestDto userRequestDto){
        for(long id : userRequestDto.getIds())
            paperAnswerRepository.delete(id);
    }
}
