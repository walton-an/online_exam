package com.online.examing.controller;

import com.domain.ExamPaper;
import com.domain.PaperAnswer;
import com.online.examing.Routes;
import com.online.examing.domain.PaperRequestDto;
import com.online.examing.domain.UserRequestDto;
import com.online.examing.service.ExamService;
import com.online.examing.service.PaperAnswerService;
import com.online.examing.repository.ExamRepository;
import com.online.examing.repository.PaperAnswerRepository;
import com.online.examing.service.PaperService;
import com.utils.RestCode;
import com.utils.RestDoing;
import com.utils.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: walton
 * @Description:
 * @Createtime: 2018/1/30
 */
@RestController
@RequestMapping(Routes.PAPER_BASE_ROUTE)
public class PaperController {
    @Autowired
    private ExamService examService;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private PaperAnswerService paperAnswerService;
    @Autowired
    private PaperAnswerRepository paperAnswerRepository;
    @Autowired
    private PaperService paperService;

    Logger logger = LoggerFactory.getLogger(PaperController.class);
    /**
     * 生成试卷
     */
    @PostMapping(Routes.CREATE_UPDATE_PAPER)
    public ExamPaper addExam(@RequestBody ExamPaper examPaper){
        return examService.saveExam(examPaper);
    }

    @GetMapping(Routes.GET_PAPER)
    public Map getExam(PaperRequestDto paperRequestDto){
        Map map = paperService.getExamPaper(paperRequestDto);
        return map;
//        List<ExamPaper> list;
//        if(teacherId == 0)
//            list = examRepository.findAll();
//        else
//            list = examRepository.findByTeacherId(teacherId);
//        return list;
    }

    @GetMapping(Routes.GET_ALL_PAPER)
    public List<ExamPaper> getAll(){
        return examRepository.findAll();
    }

    @GetMapping(Routes.GET_PAPEER_BY_ID)
    public ExamPaper getExamPaperById(@RequestParam(value = "examId")String examId){
        return examRepository.findById(Long.parseLong(examId));
    }

    @PostMapping(Routes.ADD_PAPER_SCORE)
    public RestResult addPaperScore(@RequestBody PaperRequestDto paperRequestDto){
        RestDoing restDoing = restResult ->{
            paperAnswerService.addPaperScore(paperRequestDto);
            restResult.data = RestCode.CD1;
        };
        return restDoing.go(null, logger);
    }

    @PostMapping(Routes.ADD_PAPER_ANSWER)
    public RestResult addPaperAnswer(@RequestBody PaperAnswer paperAnswer){
        RestDoing restDoing = restResult ->{
            paperAnswerService.addPaperAnswer(paperAnswer);
            restResult.data = RestCode.CD1;
        };
        return restDoing.go(null, logger);
    }


    @GetMapping(Routes.EXIST_PAPER_ANSWER)
    public PaperAnswer existPaperAnswer(@RequestParam(value = "stuId")String stuId,@RequestParam(value = "paperId")String paperId){
        return paperAnswerRepository.findByStuIdAndPaperId(Long.valueOf(stuId), Long.valueOf(paperId));
    }

    @GetMapping(Routes.GET_PAPER_ANSWER)
    public RestResult getPaperAnswer(PaperRequestDto paperRequestDto){
        RestDoing restDoing = restResult ->{
            restResult.data =  paperAnswerService.getPaperAnswer(paperRequestDto);
        };
        return restDoing.go(null, logger);
    }

    @GetMapping(Routes.GET_PAPER_ANSWER_DETAIL)
    public RestResult getPaperAnswerDetail(PaperRequestDto paperRequestDto){
        RestDoing restDoing = restResult ->{
            restResult.data =  paperAnswerService.getPaperAnswerDetail(paperRequestDto.getPaperId());
        };
        return restDoing.go(null, logger);
    }

    @GetMapping(Routes.GET_GROUP_PAPER_ANSWER)
    public RestResult getGroupPaperAnswer(PaperRequestDto paperRequestDto){
        RestDoing restDoing = restResult ->{
            restResult.data =  paperAnswerService.getGroupPaperAnswer(paperRequestDto);
        };
        return restDoing.go(null, logger);
    }


    /**
     * 删除考生答卷
     */
    @PostMapping(Routes.DELETE_PAPER_ANSWER)
    public RestResult delete(@RequestBody UserRequestDto userRequestDto){
        RestDoing restDoing = restResult ->{
            paperAnswerService.delete(userRequestDto);
            restResult.data = RestCode.CD1;
        };
        return restDoing.go(null, logger);
    }


}
