package com.online.examing.controller;

import com.domain.User;
import com.online.examing.Routes;
import com.online.examing.domain.PaperRequestDto;
import com.online.examing.domain.UserRequestDto;
import com.online.examing.service.UserService;
import com.utils.RestCode;
import com.utils.RestDoing;
import com.utils.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Routes.USER_BASE_ROUTE)
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping(Routes.USER_LOGIN)
    public RestResult login(@RequestBody User user){
        RestDoing restDoing = restResult ->{
            restResult.data = userService.login(user);
        };
        return restDoing.go(null, logger);
    }

    /**
     * 用户注册
     */
    @PostMapping(Routes.USER_REGISTER)
    public RestResult register(@RequestBody UserRequestDto userRequestDto){
        RestDoing restDoing = restResult ->{
            restResult.data = userService.register(userRequestDto);
        };
        return restDoing.go(null, logger);
    }

    /**
     * 用户更新信息
     */
    @PostMapping(Routes.USER_UPDATE_INFO)
    public RestResult updateInfo(@RequestBody PaperRequestDto paperRequestDto){
        RestDoing restDoing = restResult ->{
            restResult.data = userService.updateInfo(paperRequestDto);
        };
        return restDoing.go(null, logger);
    }

    /**
     * 获取用户信息
     */
    @GetMapping(Routes.USER_GET_INFO)
    public RestResult getInfo(User user){
        RestDoing restDoing = restResult ->{
            restResult.data = userService.getInfo(user);
        };
        return restDoing.go(null, logger);
    }

    /**
     * 获取所有用户信息
     */
    @GetMapping(Routes.USER_GET_ALL_INFO)
    public RestResult getInfo(UserRequestDto user){
        RestDoing restDoing = restResult ->{
            restResult.data = userService.getAllInfo(user);
        };
        return restDoing.go(null, logger);
    }

    /**
     * 获取用户的试卷
     */
    @GetMapping(Routes.USER_GET_PAPER)
    public RestResult getPaper(@RequestParam(value = "examClass") String examClass,@RequestParam(value = "stuId") String stuId){
        RestDoing restDoing = restResult ->{
            restResult.data = userService.getPaper(examClass,stuId);
        };
        return restDoing.go(null, logger);
    }

    /**
     * 删除用户
     */
    @PostMapping(Routes.USER_DELETE)
    public RestResult delete(@RequestBody UserRequestDto userRequestDto){
        RestDoing restDoing = restResult ->{
             userService.delete(userRequestDto);
             restResult.data = RestCode.CD1;
        };
        return restDoing.go(null, logger);
    }

    /**
     * 删除用户
     */
    @PostMapping(Routes.USER_UPDATE_STATUS)
    public RestResult updateStatus(@RequestBody UserRequestDto userRequestDto){
        RestDoing restDoing = restResult ->{
            userService.updateStatus(userRequestDto);
            restResult.data = RestCode.CD1;
        };
        return restDoing.go(null, logger);
    }

    /**
     * 查看用户名是否存在
     */
    @GetMapping(Routes.USER_CHECK_NUMBER)
    public RestResult checkExist(UserRequestDto userRequestDto){
        RestDoing restDoing = restResult ->{
            restResult.data = userService.ifExist(userRequestDto);
        };
        return restDoing.go(null, logger);
    }


}
