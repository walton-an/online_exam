<template>
    <section>
        <el-col :span="24" class="toolbar">
            <el-form ref="exam"  :model="examPaper"  label-width="100px" class="demo-ruleForm">
                <el-form-item  >
                    <el-table :data="allPaperAnswer" highlight-current-row  @selection-change="selsChange" style="width: 100%;" ref="examRef" >
                        <el-table-column property="paperName" label="测试名称" width="200"></el-table-column>
                        <el-table-column property="studentName" label="考生名"></el-table-column>
                        <el-table-column property="managerClass" label="考生班级" ></el-table-column>
                        <el-table-column property="teacherName" label="出卷老师" ></el-table-column>
                        <el-table-column property="status" label="评分状态">评分
                            <template scope="scope">
                                <div v-if="scope.row.hasMark === 1" >
                                    {{scope.row.score}} 分
                                </div>
                                <div v-else style="color:blue;cursor: pointer" @click="startMark(scope.$index, scope.row)">
                                    评分
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>

                </el-form-item>
            </el-form>
            <el-dialog :title="title" :visible="paperVisible" :show-close="false" :fullscreen="true" > <p align="right" style="color: red;font-weight:bold;" >
                <div>
                    <span align="right" style="color: green;font-weight:bold;" >考生答案：{{this.stuAnswer}}</span><br><br>
                    <span align="right" style="color: green;font-weight:bold;" >正确答案：{{this.rightAnswer}}</span>
                </div>
                <div><br>
                    <span style="font-weight:bold;">{{this.detailPaperAnswer.title}}</span><span style="color: red"> （{{this.detailPaperAnswer.score}}分）   </span><br><br>
                    <el-input placeholder="请输入您的评分" v-model="score" clearable v-on:blur="checkScore()"></el-input>
                </div>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="close()">关 闭</el-button>
                    <el-button type="primary" @click="submitPaper()" v-text="buttonName"></el-button>
                </div>
            </el-dialog>
        </el-col>
    </section>
</template>
<script>
    import util from '../../common/js/util';
    import {
        addPaperAnswer, addPaperScore,
        existPaperAnswer,
        getGroupPaperAnswer,
        getInfo,
        getPaper,
        getPaperAnswer, getPaperAnswerDetail
    } from "../../api/api";
    export default {
        data(){
            return {
                studentInfo: {},
                accountNumber: sessionStorage.getItem('accountNumber'),
                allPaperAnswer: [
                    {
                        status: 0
                    }
                ],
                examPaper: {},
                paperVisible: false,
                examQuestions: [],
                selectAnswers: [],
                tfAnswers: [],
                fullAnswer: [],
                title: "",
                minutes:0,
                seconds:0,
                paperId: "",
                ifTest: 0,
                score: "",
                answer: [],
                managerClass: '',
                studentName: sessionStorage.getItem('name'),
                paperName: '',
                examClass: [],
                teacherName: '',
                timeHandler: "",
                detailPaperAnswer: {},
                buttonName: "评 分",
                stuAnswer: "",
                rightAnswer: "",
                hasMark: 0,
            };
        },
        methods:{
            num:function (n) {
                return n<10 ? "0" + n : "" + n
            },
            checkScore: function () {
                if(this.score < 0 || this.score > this.detailPaperAnswer.score){
                    this.$message({
                        message: "分数应该在0-" + this.detailPaperAnswer.score + "之间，请重新输入",
                        type: 'error'
                    });
                    this.score = "";
                    return false;
                }
            },
            //倒计时
            timer:function () {
                var _this = this;
                this.timeHandler = window.setInterval(function () {
                    if (_this.seconds == 0 && _this.minutes != 0) {
                        _this.seconds = 59;
                        _this.minutes -= 1;
                    }else if(_this.minutes == 0 && _this.seconds == 0){
                        _this.submitPaper();
                        _this.seconds = 0;
                        window.clearInterval(this.timeHandler);
                    }else{
                        _this.seconds -= 1
                    }

                },1000);
            },
            startMark: function (index, row) {
                this.paperId = $.extend(true, {}, row).paperIdStr;
                var para = {
                    paperId: this.paperId,
                };
                this.buttonName = "评 分";
                this.score = "";
                if(!_.isUndefined($.extend(true, {}, row).fullAnswer[0])){
                    this.stuAnswer = $.extend(true, {}, row).fullAnswer[0].toString();
                }else{
                    this.stuAnswer = "";
                }

                //查找是否已评分
                var markUser = $.extend(true, {}, row).markUsers;
                if(!_.isUndefined(markUser)){
                    var accountNumber  = this.accountNumber + "";
                    for(var i = 0; i < markUser.length; i++){
                        if(markUser[i].userIdStr.localeCompare(accountNumber) == 0){
                            this.score = markUser[i].score;
                            this.buttonName = "更新评分";
                        }
                    }
                }
                this.stuId = $.extend(true, {}, row).stuIdStr;
                getPaperAnswerDetail(para).then(res=>{
                    this.detailPaperAnswer = res.data.examQuestion[0];
                    this.rightAnswer = res.data.examQuestion[0].blankAnswers[0].toString();
                });
                this.paperVisible = true;
            },
            formatTime: function (row, column) {
                var newDate = new Date();
                newDate.setTime(row[column.property]);
                return util.formatDate.format(newDate, 'yyyy-MM-dd hh:mm:ss');
            },
            selsChange: function () {

            },
            close: function () {
                this.paperVisible = false;
            },
            getStudentPaperAnswer: function () {
                let para = {
                    accountNumber: this.accountNumber
                };
                getInfo(para).then(res=>{
                    this.studentInfo = res.data;
                    this.managerClass = this.studentInfo.managerClasses[0].grade + this.studentInfo.managerClasses[0].major[0];
                    if(!_.isUndefined(this.studentInfo.managerClasses)) {
                        let paperPara = {
                            stuId: this.accountNumber,
                            managerClass: this.studentInfo.managerClasses[0].grade + this.studentInfo.managerClasses[0].major[0],
                            group: this.studentInfo.group,
                        };
                        getGroupPaperAnswer(paperPara).then(res => {
                            this.allPaperAnswer = res.data.list;
//                            //查找是否已评分
                            for(var i = 0; i< this.allPaperAnswer.length; i++){
                                var markUser = this.allPaperAnswer[i].markUsers;
                                if(!_.isUndefined(markUser)){
                                    var accountNumber  = this.accountNumber + "";
                                    for(var j = 0; j < markUser.length; j++){
                                        if(markUser[j].userIdStr.localeCompare(accountNumber) == 0){
                                            this.allPaperAnswer[i].score = markUser[j].score;
                                            this.allPaperAnswer[i].hasMark = 1;
                                        }
                                    }
                                }
                            }
                        })
                    }else {
                        this.allPaperAnswer = [];
                    }
                });
            },
            submitPaper: function () {
//                console.log("shoudong tiajiao")
//                window.clearInterval(this.timeHandler);
//                this.minutes = 0;
//                this.seconds = 0;
                setTimeout(() => {
                    if(this.score === ""){
                        this.$message({
                            message: '请输入您的评分',
                            type: 'error'
                        });
                        return;
                    }
                    var para = {
                        markUserId: this.accountNumber,
                        userName: this.studentName,
                        score: this.score,
                        paperId: this.paperId,
                        stuId: this.stuId,
                    };
                    addPaperScore(para).then(res=>{
                        this.$message({
                            message: '评分成功',
                            type: 'success'
                        });
                        this.getStudentPaperAnswer();
                        this.paperVisible = false;
                    })
                }, 500);
            },
        },
        watch:{
            second:{
                handler(newVal){
                    this.num(newVal)
                }
            },
            minute:{
                handler(newVal){
                    this.num(newVal)
                }
            }
        },
        computed:{
            second:function () {
                return this.num(this.seconds)
            },
            minute:function () {
                return this.num(this.minutes)
            }
        },
        mounted(){
            this.getStudentPaperAnswer();
        },
    }
</script>
<style scoped>
    .moreContent {
        color: blue;
        cursor: pointer
    }
</style>