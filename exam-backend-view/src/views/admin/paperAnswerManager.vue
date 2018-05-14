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
            <el-table-column prop="stuId" label="学号" width="120"></el-table-column>
            <el-table-column prop="studentName" label="姓名" width="100"></el-table-column>
            <el-table-column prop="managerClass" label="班级" width="130"></el-table-column>
            <el-table-column prop="score" label="分数" width="80" ></el-table-column>
            <el-table-column prop="paperName" label="测试名称"></el-table-column>
            <el-table-column prop="teacherName" label="出卷老师" width="120" ></el-table-column>
            <el-table-column prop="examClass" label="考试班级" width="400" :formatter="formatClass"></el-table-column>
            <el-table-column prop="createTime" label="交卷时间" :formatter="formatTime" width="180"></el-table-column>
            <!--<el-table-column label="设为" width="80">-->
                <!--<template scope="scope">-->
                    <!--<div v-if="scope.row.status === 0" style="color:blue;cursor: pointer" @click="updateStatus(scope.$index, scope.row,1)">显示</div>-->
                    <!--<div v-if="scope.row.status === 1" style="color:red;cursor: pointer" @click="updateStatus(scope.$index, scope.row,0)">隐藏</div>-->
                <!--</template>-->
            <!--</el-table-column>-->
        </el-table>
        <el-col :span="24" class="toolbar">
            <el-form :inline="true"  v-model="operations">
                <el-form-item>
                    <el-button type="danger" @click="deletePaperAnswer" :disabled="this.selectedItems.length===0">删除</el-button>
                </el-form-item>
                <el-pagination layout="total,prev, pager, next" @current-change="handleCurrentChange" :page-size="pageSize" :total="total" style="float:right;"></el-pagination>
            </el-form>
        </el-col>
    </section>
</template>
<script>
    import util from '../../common/js/util';
    import {
        deletePaperAnswer, getAllExamPaper,
        getAllInfo, getExamPaper,
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
                selectedItems: "",
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
                    page: this.page,
                    pageSize: this.pageSize
                };
                if(this.paperIdStr != ""){
                    para.paperId = this.paperIdStr;
                }
                getPaperAnswer(para).then(res=>{
                    this.allPaperAnswer = res.data.list;
                    this.total = res.data.size;
                })
            },
            handleCurrentChange: function (val) {
                this.page = val;
                this.getAllPaperAnswer();
            },
            getAllPaperTitle: function () {
                getAllExamPaper().then(res=>{
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