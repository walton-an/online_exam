package com.online.examing.domain;

import com.domain.ManagerClass;

import java.util.List;

/**
 * @Author: walton
 * @Description:
 * @Createtime: 2018/3/27
 */
public class UserRequestDto {
    public int page;
    public int pageSize;
    private Long accountNumber;
    private Long oldAccountNum;
    private String name;
    private String password;
    private Integer type;   //0学生、1教师、2管理员
    private String grade;
    private String school;
    private List<String> major;
    private List<Long> ids;
    private int status;
    private long id;
    private int searchType;
    private String searchTitle;
    private List<ManagerClass> managerClasses;
    private int group;

    public Long getOldAccountNum() {
        return oldAccountNum;
    }

    public void setOldAccountNum(Long oldAccountNum) {
        this.oldAccountNum = oldAccountNum;
    }

    public List<ManagerClass> getManagerClasses() {
        return managerClasses;
    }

    public void setManagerClasses(List<ManagerClass> managerClasses) {
        this.managerClasses = managerClasses;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public List<String> getMajor() {
        return major;
    }

    public void setMajor(List<String> major) {
        this.major = major;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
