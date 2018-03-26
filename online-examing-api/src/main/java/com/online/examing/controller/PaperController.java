package com.online.examing.controller;

import com.domain.PaperQuestion;
import com.online.examing.Routes;
import com.online.examing.domain.PaperRequestDto;
import com.online.examing.repository.PaperRepository;
import com.online.examing.service.PaperService;
import com.utils.RestDoing;
import com.utils.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(Routes.PAPER_BASE_ROUTE)
public class PaperController {

    private final static Logger logger = LoggerFactory.getLogger(PaperController.class);

    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperRepository paperRepository;


    /**
     * 添加、更新题目
     */
    @PostMapping(Routes.PAPER_ADDQUESTION)
    public RestResult addQuestion(@RequestBody PaperQuestion paperDetail){
        RestDoing restDoing = restResult ->{
            restResult.data = paperService.addQuestion(paperDetail);
        };
        return restDoing.go(null, logger);
    }

    /**
     * 删除题目
     */
    @PostMapping(Routes.PAPER_DELETEQUESTION)
    public RestResult deleteQuestion(@RequestParam(value="id") Long  id){
        RestDoing restDoing = restResult ->{
            restResult.data = paperService.deleteQuestion(id);
        };
        return restDoing.go(null, logger);
    }

    /**
     * 获取题目的分页列表
     */
    @GetMapping(Routes.PAPER_GETQUESTIONDLIST)
    public RestResult getQuestionList(PaperRequestDto paperRequestDto){
        RestDoing restDoing = restResult ->{
            restResult.data = paperService.getPaperList(paperRequestDto);
        };
        return restDoing.go(null, logger);
    }

    /**
     * 获取题目的总数目
     */
    @GetMapping(Routes.PAPER_GETQUESTIONDSIZE)
    public RestResult getQuestionSize(@RequestParam(value = "type")Integer type){
        RestDoing restDoing = restResult ->{
            restResult.data = paperService.getPaperSize(type);
        };
        return restDoing.go(null, logger);
    }

    /**
     * 查找题目
     */
    @GetMapping(Routes.PAPER_SEARCHQUESTION)
    public RestResult searchQuestion(PaperRequestDto paperRequestDto){
        RestDoing restDoing = restResult ->{
            restResult.data = paperService.searchQuestion(paperRequestDto);
        };
        return restDoing.go(null, logger);
    }

    /**
    *@Description: 更新信息
    *@Date: 2017/12/8
    */
    @PostMapping(Routes.PAPER_UPDATEQUESTION)
    public RestResult updateQuestion(@RequestBody PaperRequestDto paperRequestDto){
        RestDoing restDoing = restResult ->{
            String[] id = paperRequestDto.getIds().split(",");
            Integer status = paperRequestDto.getStatus();
            if(status==2){
                for (String i : id) {
                    paperRepository.delete(Long.valueOf(i));
                }
            }else {
                PaperQuestion paperQuestion = new PaperQuestion();
                for (String i : id) {
                    paperQuestion = paperRepository.findOne(Long.valueOf(i));
                    paperQuestion.setStatus(status);
                    restResult.data = paperRepository.save(paperQuestion);
                }
            }
        };
        return restDoing.go(null, logger);
    }



}
