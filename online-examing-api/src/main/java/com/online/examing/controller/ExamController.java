package com.online.examing.controller;

import com.domain.ExamPaper;
import com.domain.PaperAnswer;
import com.online.examing.Routes;
import com.online.examing.service.ExamService;
import com.online.examing.service.PaperAnswerService;
import com.online.examing.repository.ExamRepository;
import com.online.examing.repository.PaperAnswerRepository;
import com.utils.RestCode;
import com.utils.RestDoing;
import com.utils.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: walton
 * @Description:
 * @Createtime: 2018/1/30
 */
@RestController
@RequestMapping(Routes.EXAM_BASE_ROUTE)
public class ExamController {
    @Autowired
    private ExamService examService;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private PaperAnswerService paperAnswerService;
    @Autowired
    private PaperAnswerRepository paperAnswerRepository;

    Logger logger = LoggerFactory.getLogger(ExamController.class);
    /**
     * 生成试卷
     */
    @PostMapping(Routes.EXAM_CREATE_UPDATE)
    public ExamPaper addExam(@RequestBody ExamPaper examPaper){
        return examService.saveExam(examPaper);
    }

    @GetMapping(Routes.EXAM_GET)
    public List<ExamPaper> getExam(@RequestParam(value = "teacherId")long teacherId){
        return examRepository.findByTeacherId(teacherId);
    }

    @GetMapping(Routes.EXAM_GET_PAPEER_BY_ID)
    public ExamPaper getExamPaperById(@RequestParam(value = "examId")String examId){
        return examRepository.findById(Long.parseLong(examId));
    }

    @PostMapping(Routes.EXAM_ADD_PAPER_ANSWER)
    public RestResult addPaperAnswer(@RequestBody PaperAnswer paperAnswer){
        RestDoing restDoing = restResult ->{
            paperAnswerService.addPaperAnswer(paperAnswer);
            restResult.data = RestCode.CD1;
        };
        return restDoing.go(null, logger);
    }

    @GetMapping(Routes.EXAM_EXIST_PAPER_ANSWER)
    public PaperAnswer existPaperAnswer(@RequestParam(value = "stuId")String stuId,@RequestParam(value = "paperId")String paperId){
        return paperAnswerRepository.findByStuIdAndPaperId(Long.valueOf(stuId), Long.valueOf(paperId));
    }
}
