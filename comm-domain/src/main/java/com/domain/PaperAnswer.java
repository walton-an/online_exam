package com.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/**
 * @Author: walton
 * @Description: 试卷的答题卡
 * @Createtime: 2017/11/28
 */

@Document(collection="PaperAnswer")
public class PaperAnswer extends BaseEntity{
    private Long stuId;
    private String studentName;
    private String managerClass;
    private String teacherName;
    private long teacherId;
    private List<String> examClass;
    private String paperName;
    private Long paperId;
    private List<String> selectAnswers;
    private List<String> tfAnswers;
    private List<String> fullAnswer;
    private double score;
    private long createTime;
    private int group;
    private List<MarkUser> markUsers;
    private PaperQuestion question;

    public Long getStuId() {
        return stuId;
    }

    public String getStuIdStr() {
        return this.stuId + "";
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    public Long getPaperId() {
        return paperId;
    }

    public String getPaperIdStr() {
        return this.paperId + "";
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public List<String> getSelectAnswers() {
        return selectAnswers;
    }

    public void setSelectAnswers(List<String> selectAnswers) {
        this.selectAnswers = selectAnswers;
    }

    public List<String> getTfAnswers() {
        return tfAnswers;
    }

    public void setTfAnswers(List<String> tfAnswers) {
        this.tfAnswers = tfAnswers;
    }

    public List<String> getFullAnswer() {
        return fullAnswer;
    }

    public void setFullAnswer(List<String> fullAnswer) {
        this.fullAnswer = fullAnswer;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getManagerClass() {
        return managerClass;
    }

    public void setManagerClass(String managerClass) {
        this.managerClass = managerClass;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<String> getExamClass() {
        return examClass;
    }

    public void setExamClass(List<String> examClass) {
        this.examClass = examClass;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public List<MarkUser> getMarkUsers() {
        return markUsers;
    }

    public void setMarkUsers(List<MarkUser> markUsers) {
        this.markUsers = markUsers;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public PaperQuestion getQuestion() {
        return question;
    }

    public void setQuestion(PaperQuestion question) {
        this.question = question;
    }
}
