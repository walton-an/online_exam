package com.online.examing.service;

import com.domain.ExamPaper;
import com.domain.ManagerClass;
import com.domain.User;
import com.online.examing.domain.PaperRequestDto;
import com.online.examing.domain.UserRequestDto;
import com.online.examing.repository.UserRepository;
import com.online.examing.repository.ExamRepository;
import com.online.examing.repository.PaperAnswerRepository;
import com.utils.DefaultKeyGenerator;
import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author: walton
 * @Description: 登录、注册的Service
 * @Createtime: 2017/11/28
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DefaultKeyGenerator defaultKeyGenerator ;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private PaperAnswerRepository paperAnswerRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
    *@Description: 登录成功返回用户名，失败返回空
    *@Date: 2017/11/28
    */

    public User login(User user){
        if(user.getAccountNumber() == 11111 && user.getPassword().equals("admin")){
            user.setType(2);
            return user;
        }
        User existUser = userRepository.findByAccountNumberAndStatus(user.getAccountNumber(),1);
        if(existUser != null){
            if(!user.getPassword().equals(existUser.getPassword())){
                existUser.setType(3);  //type==3代表密码错误
            }
        }else {
            User notUser = new User();
            notUser.setType(4);  //type==4代表用户不存在
            return notUser;
        }
        return existUser;
    }

    /**
    *@Description: 用户注册
    *@Date: 2017/11/28
    */
    public Long register(UserRequestDto userRequestDto){
        User user = userRepository.findByAccountNumber(userRequestDto.getAccountNumber());
        if(user == null){
            user = new User();
            user.setId((Long) defaultKeyGenerator.generateKey());
        }
        user.setAccountNumber(userRequestDto.getAccountNumber());
        user.setName(userRequestDto.getName());
        user.setPassword(userRequestDto.getPassword());
        user.setType(userRequestDto.getType());
        ManagerClass managerClass = new ManagerClass();
        managerClass.setGrade(userRequestDto.getGrade());
        managerClass.setSchool(userRequestDto.getSchool());
        managerClass.setMajor(userRequestDto.getMajor());
        List<ManagerClass> list = new ArrayList<>();
        list.add(managerClass);
        if(userRequestDto.getType() == 0)
            user.setManagerClasses(null);
        user.setManagerClasses(list);
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(System.currentTimeMillis());
        user.setStatus(1);
        userRepository.save(user);
        return user.getId();
    }

    /**
    *@Description: 更新用户信息
    *@Date: 2017/12/1
    */
    public User updateInfo(PaperRequestDto paperRequestDto){
        User existUser = userRepository.findByAccountNumber(paperRequestDto.getAccountNumber());
        existUser.setUpdateTime(System.currentTimeMillis());
        if(paperRequestDto.getPassword()!=null&&paperRequestDto.getPassword()!="") {
            existUser.setPassword(paperRequestDto.getPassword());
        }
        if(paperRequestDto.getGrade()!=null&&paperRequestDto.getSchool()!=null) {
           List<ManagerClass> managerClassList = existUser.getManagerClasses();
           //如果之前没有mangerClass的话直接添加
           if(CollectionUtils.isEmpty(managerClassList)) {
               managerClassList = new ArrayList<>();
               ManagerClass updateClasses = new ManagerClass();
               List<String> major_list = new ArrayList<>();
               for (String s : paperRequestDto.getMajor()) {
                   major_list.add(s);
               }
               updateClasses.setGrade(paperRequestDto.getGrade());
               updateClasses.setSchool(paperRequestDto.getSchool());
               updateClasses.setMajor(major_list);
               managerClassList.add(updateClasses);
               existUser.setManagerClasses(managerClassList);
           }
           //如果有的话
           else{
               List<String> major = new ArrayList<>();
               for(Iterator<ManagerClass> iter = managerClassList.iterator(); iter.hasNext();) {
                   ManagerClass m = iter.next();
                   if( m.getGrade().equals(paperRequestDto.getGrade())&&m.getSchool().equals(paperRequestDto.getSchool())){
                        major.addAll(m.getMajor());
                        iter.remove();
                   }
               }
               ManagerClass updateClasses = new ManagerClass();
               List<String> major_list = new ArrayList<>();
               for (String s : paperRequestDto.getMajor()) {
                   major_list.add(s);
               }
               major_list.addAll(major);
               List newList = new ArrayList(new HashSet(major_list));
               updateClasses.setGrade(paperRequestDto.getGrade());
               updateClasses.setSchool(paperRequestDto.getSchool());
               updateClasses.setMajor(newList);
               managerClassList.add(updateClasses);
               existUser.setManagerClasses(managerClassList);
           }
         }
        User user =  userRepository.save(existUser);
        return user;
    }

    public User getInfo(User user){
        return userRepository.findByAccountNumber(user.getAccountNumber());
    }

    public Map getAllInfo(UserRequestDto user){
        Map map = new HashMap();
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "createTime");
        Sort sort = new Sort(order);
        Pageable pageable;
        int size = Math.toIntExact(userRepository.count());
        if(size == 0){
            map.put("list", null);
            map.put("size", 0);
            return map;
        }
        if(size < user.getPageSize()) {
            pageable = new PageRequest(user.getPage()-1, size, sort);//对数据进行分页
        }else {
            pageable = new PageRequest(user.getPage()-1, user.getPageSize(), sort);//对数据进行分页
        }
        List<User> users = userRepository.findAll(pageable).getContent();
        map.put("list", CollectionUtils.isEmpty(users)?null:users);
        map.put("size", size);
        return map;
    }

    public List<ExamPaper> getPaper(String  examClass,String stuId){
        List<ExamPaper> examPaperList = examRepository.findByExamClassContains(examClass);
        for(ExamPaper examPaper : examPaperList){
            if(paperAnswerRepository.findByStuIdAndPaperId(Long.valueOf(stuId), Long.valueOf(examPaper.getIdStr()))!=null)
                examPaper.setStatus(1);
        }
        return examPaperList;
    }

    public void delete(UserRequestDto userRequestDto){
        for(long id : userRequestDto.getIds())
            userRepository.delete(id);
    }

    public void updateStatus(UserRequestDto userRequestDto){
        User user = userRepository.findOne(userRequestDto.getId());
        user.setStatus(userRequestDto.getStatus());
        userRepository.save(user);
    }

    public Boolean ifExist(UserRequestDto userRequestDto){
        User user = userRepository.findByAccountNumber(userRequestDto.getAccountNumber());
        if(user == null)
            return true;
        else
            return false;
    }

}
