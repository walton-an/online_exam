<template>
    <section>
        <el-col :span="24" class="toolbar">
            <el-form :inline="true">
                <el-form-item label="">
                    <el-select v-model="paperIdStr" placeholder="请选择测试" clearable>
                        <el-option
                                v-for="item in testTitle"
                                :key="item.idStr"
                                :value="item.idStr"
                                :label="item.title"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button icon="search" @click="getAllPaperAnswer">搜索</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <el-table :data="allPaperAnswer" highlight-current-row v-loading="listLoading" @selection-change="selectChange" style="width: 100%;" ref="userRef">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="score" label="分数" width="80" ></el-table-column>
            <el-table-column prop="stuId" label="学号" width="120"></el-table-column>
            <el-table-column prop="studentName" label="姓名" width="100"></el-table-column>
            <el-table-column prop="managerClass" label="班级" width="130"></el-table-column>
            <el-table-column prop="paperName" label="测试名称"  ></el-table-column>
            <el-table-column prop="teacherName" label="出卷老师" width="120" ></el-table-column>
            <el-table-column prop="examClass" label="测试班级"  :formatter="formatClass"></el-table-column>
            <el-table-column prop="createTime" label="交卷时间" :formatter="formatTime" ></el-table-column>
            <el-table-column prop="createTime" label="查看答卷">
                <template scope="scope">
                    <div style="color:blue;cursor: pointer" @click="showPaperQuestion(scope.$index, scope.row)">
                        查看答卷
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <el-col :span="24" class="toolbar">
            <el-form :inline="true"  v-model="operations">
                <el-form-item>
                    <el-button type="danger" @click="deletePaperAnswer" :disabled="this.selectedItems.length===0">删除</el-button>
                </el-form-item>
                <el-pagination layout="total,prev, pager, next" @current-change="handleCurrentChange" :page-size="pageSize" :total="total" style="float:right;"></el-pagination>
            </el-form>
        </el-col>

        <el-dialog title="查看答卷" v-model="editFormVisible" :close-on-click-modal="false">
            <div>
                <span align="right" style="color: black;font-weight:bold;" >题目：{{this.question}}</span><br><br>
                <span align="right" style="color: green;font-weight:bold;" >考生答案：{{this.stuAnswer}}</span><br><br>
                <span align="right" style="color: green;font-weight:bold;" >正确答案：{{this.rightAnswer}}</span><br><br>
            </div>
            <span align="right" style="color: black;font-weight:bold;" >评分同学：</span><br><br>
            <el-table :data="markUser" highlight-current-row>
                <el-table-column prop="userName" label="名字"></el-table-column>
                <el-table-column prop="userIdStr" label="学号"></el-table-column>
                <el-table-column prop="score" label="分数"></el-table-column>
            </el-table>
            <el-col :span="24" style="margin: 30px;" align="right">
                <el-col :span="12" class="text-center">
                    <el-button @click="editFormVisible=false">关闭</el-button>
                </el-col>
            </el-col>
        </el-dialog>

    </section>
</template>
<script>
    import util from '../../common/js/util';
    import {
        deletePaperAnswer,
        getExamPaper, getInfo,
        getPaperAnswer,
        searchUser,
        userDelete,
        userUpdateStatus
    } from "../../api/api";
    export default {
        data(){
            var g = this.GLOBAL.defaultConfig;
            return {
                page: 1,
                pageSize: 10,
                total: 0,
                question: "",
                stuAnswer: "",
                rightAnswer: "",
                selectedItems: "",
                accountNumber: sessionStorage.getItem('accountNumber'),
                paperIdStr: "",
                operations: {
                    oType: "1",
                    status: "",
                },
                status: g.operations,
                testTitle: [],
                gameStatus: g.gameStatus,
                roomStatus: g.roomStatus,
                rooms: [
                ],
                listLoading: false,
                selectedItems: [],
                allUser: [],
                allPaperAnswer: [],
                examClasses: [],
                editFormVisible: false,
                markUser: [],
            };
        },
        methods:{
            selectChange: function (items) {
                this.selectedItems = items;
            },
            formatType: function (row, column) {
                if(row.type == 0)
                    return "学生";
                else if(row.type == 1)
                    return "教师";
            },
            formatStatus: function (row, column) {
                if(row.status == 0)
                    return "隐藏";
                else if(row.status == 1)
                    return "显示";
            },
            formatClass: function (row, column) {
                var ret = new Array();
                for (let i=0; i<row.examClass.length; i++){
                    ret.push(row.examClass[i]);
                }
                return ret.join("、") ;
            },
            formatTime: function (row, column) {
                var newDate = new Date();
                newDate.setTime(row[column.property]);
                return util.formatDate.format(newDate, 'yyyy-MM-dd hh:mm:ss');
            },
            deletePaperAnswer: function () {
                let para = {
                    ids: this.selectedItems.map(item => item.idStr)
                };
                this.$confirm('此操作将永久删除该考生答卷, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deletePaperAnswer(para).then(res => {
                        this.getAllPaperAnswer();
                    });
                });
            },
            showPaperQuestion: function (index, row) {
                this.editFormVisible = true;
                this.question = $.extend(true, {}, row).question.title;
                this.stuAnswer = $.extend(true, {}, row).fullAnswer.toString();
                this.rightAnswer = $.extend(true, {}, row).question.blankAnswer;
                this.markUser = [];
                if(!_.isUndefined($.extend(true, {}, row).markUsers)){
                    this.markUser  = $.extend(true, {}, row).markUsers;
                }
            },
            updateStatus: function (index, row, status) {
                let para = {
                    id: row.idStr,
                    status: status
                };
                userUpdateStatus(para).then(res => {
                    this.getAllUserInfo();
                });
            },
            editUser: function (index, row) {
                this.$router.push({ path: '/addUser/?accountNumber=' + row.accountNumber})
            },
            getAllPaperAnswer: function(){
                let para = {
                    accountNumber: this.accountNumber
                };
                getInfo(para).then(res=> {
                    var managerClass = res.data.managerClasses;
                    var examClasses =  new Array();
                    managerClass.forEach(function (value1, index1, array) {
                        value1.major.forEach(function (value2, index2, array) {
                            var className = value1.grade + value1.school + value2;
                            examClasses.push(className);
                        });
                    });
                    console.log("测试班级" + examClasses.length);
                    let para = {
                        page: this.page,
                        pageSize: this.pageSize,
                        teacherId: this.accountNumber,
                        examClassStr: JSON.stringify(examClasses),
                    };
                    if(this.paperIdStr != ""){
                        para.paperId = this.paperIdStr;
                    }
                    getPaperAnswer(para).then(res=>{
                        this.allPaperAnswer = res.data.list;
                        this.total = res.data.size;
                    })
                });
            },
            searchList: function () {
                let para = {
                    searchType: this.filters.type,
                    searchTitle: this.filters.searchTitle,
                    page: this.page,
                    pageSize: this.pageSize
                };
                searchUser(para).then(res=>{
                    if(res.data.list!=null){
                        this.allUser = res.data.list;
                        this.total = res.data.size;
                    }else{
                        this.allUser = [];
                        this.total = 0;
                    }
                })
            },
            handleCurrentChange: function (val) {
                this.page = val;
                this.getAllPaperAnswer();
            },
            getAllPaperTitle: function () {
                let para = {
                    teacherId: this.accountNumber
                };
                getExamPaper(para).then(res=>{
                    this.testTitle = res;
                });
            },
        },

        mounted(){
          this.getAllPaperAnswer();
          this.getAllPaperTitle();
        },
        components:{}
    }
</script>
<style scoped>
    .moreContent {
        color: blue;
        cursor: pointer
    }
</style>