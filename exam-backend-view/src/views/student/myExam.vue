<template>
    <section>
        <el-col :span="24" class="toolbar">
            <el-form ref="exam"  :model="examPaper"  label-width="100px" class="demo-ruleForm">
                <el-form-item  >
                    <el-table :data="allPaper" highlight-current-row  @selection-change="selsChange" style="width: 100%;" ref="examRef" >
                        <el-table-column property="title" label="测试名称" width="200"></el-table-column>
                        <el-table-column property="beginTime" label="测试开始时间" :formatter="formatTime"></el-table-column>
                        <el-table-column property="endTime" label="测试结束时间" :formatter="formatTime"></el-table-column>
                        <el-table-column property="testTime" label="测试时长（/分钟）"></el-table-column>
                        <el-table-column property="score" label="分数" :formatter="formatDetailScore"></el-table-column>
                        <el-table-column property="status" label="测试状态">
                            <template scope="scope">
                                <div v-if="scope.row.status === 0" style="color:blue;cursor: pointer" @click="startTest(scope.$index, scope.row)">
                                    开始测试
                                </div>
                                <div v-if="scope.row.status === 1" style="color:blue;cursor: pointer" @click="checkPaper(scope.$index, scope.row)">
                                    查看测试题
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>

                </el-form-item>
            </el-form>
            <el-dialog :title="title" :visible="paperVisible" :show-close="false" :fullscreen="true" > <p align="right" style="color: red;font-weight:bold;" >
                <div v-if="this.ifTest === 0">
                    <span align="right" style="color: red;font-weight:bold;" >剩余时间{{minutes}}:{{seconds}}</span>
                </div>
                <div v-else>
                    <span align="right" style="color: red;font-weight:bold;" >分数：{{this.score}}分</span><br/><br/>
                    正确答案：<span align="right" style="color: green;font-weight:bold;" >{{this.answer.toString()}}</span>
                </div>
                <div v-for="(question,index) in examQuestions" :key="question.title"><br>
                    {{index+1}}、<span style="font-weight:bold;">{{question.title}}</span><span style="color: red">   （{{question.score}}分）</span><br><br>
                   <div v-if="question.type === 0">
                        <el-radio v-model="selectAnswers[index]" label="A" border size="medium">{{question.selectionA}}</el-radio><br><br>
                        <el-radio v-model="selectAnswers[index]" label="B" border size="medium">{{question.selectionB}}</el-radio><br><br>
                        <el-radio v-model="selectAnswers[index]" label="C" border size="medium">{{question.selectionC}}</el-radio><br><br>
                        <el-radio v-model="selectAnswers[index]" label="D" border size="medium">{{question.selectionD}}</el-radio><br><br>
                   </div>
                    <div v-if="question.type === 1">
                        <el-radio v-model="tfAnswers[index]" label="T" border size="medium">对</el-radio><br><br>
                        <el-radio v-model="tfAnswers[index]" label="F" border size="medium">错</el-radio><br><br>
                    </div>
                    <div v-if="question.type === 2">
                        <el-input v-model="fullAnswer[index]" clearable></el-input>
                    </div>
                </div><br>
                <span align="right" style="color: black;font-weight:bold;" v-show="markUserVisiable">评分同学：</span><br><br>
                <el-table :data="markUser" highlight-current-row v-show="markUserVisiable">
                    <el-table-column prop="userName" label="名字"></el-table-column>
                    <el-table-column prop="userIdStr" label="学号"></el-table-column>
                    <el-table-column prop="score" label="分数"></el-table-column>
                </el-table>
                <div v-if="this.ifTest === 0" slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="submitPaper()">交 卷</el-button>
                </div>
                <div v-else slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="close()">关闭</el-button>
                </div>
            </el-dialog>
        </el-col>
    </section>
</template>
<script>
    import util from '../../common/js/util';
    import {addPaperAnswer, existPaperAnswer, getInfo, getPaper} from "../../api/api";
    export default {
        data(){
            return {
                studentInfo: {},
                accountNumber: sessionStorage.getItem('accountNumber'),
                allPaper: [
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
                question: {},
                title: "",
                minutes:0,
                seconds:0,
                paperId: "",
                ifTest: 0,
                score: -1,
                answer: [],
                managerClass: '',
                studentName: sessionStorage.getItem('name'),
                paperName: '',
                examClass: [],
                teacherName: '',
                teacherId: '',
                timeHandler: "",
                markUser: [],
                markUserVisiable: false
            };
        },
        methods:{
            num:function (n) {
                return n<10 ? "0" + n : "" + n
            },
            //倒计时
            timer:function () {
                var _this = this;
                this.timeHandler = window.setInterval(function () {
                    if (_this.seconds == 0 && _this.minutes != 0) {
                        _this.seconds = 59;
                        _this.minutes -= 1;
                    }else if(_this.minutes == 0 && _this.seconds == 0){
                        console.log("timer tiajiao")
                        _this.submitPaper();
                        _this.seconds = 0;
                        window.clearInterval(this.timeHandler);
                    }else{
                        _this.seconds -= 1
                    }

                },1000);
            },
            formatScore: function () {
                if(this.score === -1)
                    return "暂无评分"
                else
                    return this.score;
            },
            formatDetailScore: function (row, column) {
                if(row[column.property]  === -1)
                    return "暂无评分"
                else
                    return row[column.property];
            },
            startTest: function (index, row) {
                var beginTime = $.extend(true, {}, row).beginTime;
                var endTime = $.extend(true, {}, row).endTime;
                var testTime = $.extend(true, {}, row).testTime;
                if(new Date().getTime() < beginTime ){
                    this.$message({
                        message: '尚未到考试时间，请稍后重试',
                        type: 'error'
                    });
                    return;
                }
                this.paperName = $.extend(true, {}, row).title;//试卷名称
                this.examClass = $.extend(true, {}, row).examClass; //考试班级
                this.teacherName =  $.extend(true, {}, row).teacherName; //出卷老师
                this.teacherId = $.extend(true, {}, row).teacherId;
                this.paperVisible = true;
                this.examQuestions = $.extend(true, {}, row).examQuestion;
                this.question = $.extend(true, {}, row).examQuestion[0];
                this.title  = $.extend(true, {}, row).title;
                this.paperId = $.extend(true, {}, row).idStr;
                this.ifTest = $.extend(true, {}, row).status;
                this.selectAnswers = [];
                this.tfAnswers = [];
                this.fullAnswer = [];
                this.score = -1;
                this.markUserVisiable = false;
                if(new Date().getTime() > endTime ){
                    console.log("考试时间已结束")
                    this.paperId = $.extend(true, {}, row).idStr;
                    this.submitPaper();
                    setTimeout(() => {
                        this.$message({
                            message: '考试时间已结束，下次请注意',
                            type: 'error'
                        });
                    }, 500);
//                    this.paperVisible = false;
                    return;
                }
                //毫秒数转为分钟数，并四舍五入
                this.minutes = Math.round(( new Date().getTime() - beginTime ) * 0.0000167);
                if(this.minutes == 0) {
                    this.seconds = Math.round(( new Date().getTime() - beginTime ) * 0.001);
                } else if(this.minutes > testTime) {
                    this.minutes = testTime;
                }
                this.timer();
                console.log(this.examQuestions.length+"长度"+this.minutes+"  "+this.seconds);
            },
            checkPaper: function (index, row) {
                this.paperVisible = true;
                this.markUserVisiable = true;
                this.examQuestions = $.extend(true, {}, row).examQuestion;
                this.title  = $.extend(true, {}, row).title;
                this.paperId = $.extend(true, {}, row).idStr;
                this.ifTest = $.extend(true, {}, row).status;
                this.answer = [];
                for(var i = 0; i < this.examQuestions.length; i++){
                    if(this.examQuestions[i].type == 0){
                        this.answer.push(this.examQuestions[i].choiceAnswer)
                    }else if(this.examQuestions[i].type == 1){
                        this.answer.push(this.examQuestions[i].tfAnswer)
                    }else if(this.examQuestions[i].type == 2){
                        this.answer.push(this.examQuestions[i].blankAnswer)
                    }
                }
                var para = {
                    stuId: this.accountNumber,
                    paperId: this.paperId,
                }
                existPaperAnswer(para).then(res=>{
                    this.selectAnswers = res.selectAnswers;
                    this.tfAnswers = res.tfAnswers;
                    this.fullAnswer = res.fullAnswer;
                    this.score = res.score;
                    this.markUser = [];
                    if(!_.isUndefined(res.markUsers)) {
                        this.markUser = res.markUsers;
                    }
                })
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
            getStudentPaper: function () {
                let para = {
                    accountNumber: this.accountNumber
                };
                getInfo(para).then(res=>{
                    this.studentInfo = res.data;
                    this.managerClass = this.studentInfo.managerClasses[0].grade + this.studentInfo.managerClasses[0].major[0];
                    if(!_.isUndefined(this.studentInfo.managerClasses)) {
                        let paperPara = {
                            stuId: this.accountNumber,
                            examClass: this.studentInfo.managerClasses[0].grade + this.studentInfo.managerClasses[0].school + this.studentInfo.managerClasses[0].major[0],
                        };
                        getPaper(paperPara).then(res => {
                            this.allPaper = res.data;
                        })
                    }else {
                        this.allPaper = [];
                    }
                });
            },
            submitPaper: function () {
                window.clearInterval(this.timeHandler);
                this.minutes = 0;
                this.seconds = 0;
                var para = {
                    stuId: this.accountNumber,
                    studentName: this.studentName,
                    managerClass: this.managerClass,//学生班级
                    teacherName: this.teacherName,//出卷老师
                    teacherId: this.teacherId,
                    examClass: this.examClass,
                    paperName: this.paperName,
                    paperId: this.paperId,
                    selectAnswers: this.selectAnswers,
                    tfAnswers: this.tfAnswers,
                    fullAnswer: this.fullAnswer,
                    group: this.studentInfo.group,
                    question: this.question,
                };
                addPaperAnswer(para).then(res=>{
                    this.$message({
                        message: '交卷成功',
                        type: 'success'
                    });
                    this.getStudentPaper();
                    this.paperVisible = false;
                })
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
            this.getStudentPaper();
        },
    }
</script>
<style scoped>
    .moreContent {
        color: blue;
        cursor: pointer
    }
</style>