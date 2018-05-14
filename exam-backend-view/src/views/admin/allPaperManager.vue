<template>
    <section>
        <br>
        <el-form ref="exam"  :model="examPaper"  class="demo-ruleForm">
            <el-form-item  >
                <el-table :data="allPaper" highlight-current-row  @selection-change="selsChange" style="width: 100%;" ref="examRef" >
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column property="title" label="测试名称" width="200"></el-table-column>
                    <el-table-column property="teacherName" label="出题教师" width="150"></el-table-column>
                    <el-table-column property="teacherId" label="教师工号" width="150"></el-table-column>
                    <el-table-column property="beginTime" label="测试开始时间" :formatter="formatTime"></el-table-column>
                    <el-table-column property="endTime" label="测试结束时间" :formatter="formatTime"></el-table-column>
                    <el-table-column property="testTime" label="测试时长(/分钟)"></el-table-column>
                    <el-table-column property="examClass" label="测试班级" :formatter="formatExamClass"></el-table-column>
                </el-table>
                <el-col :span="24" class="toolbar">
                    <el-form :inline="true">
                        <el-form-item>
                            <el-button type="danger" @click="deletePaper" :disabled="this.selectedItems.length===0">删除</el-button>
                        </el-form-item>
                        <el-pagination layout="total,prev, pager, next" @current-change="handleCurrentChange" :page-size="pageSize" :total="total" style="float:right;"></el-pagination>
                    </el-form>
                </el-col>
            </el-form-item>
        </el-form>

    </section>
</template>
<script>
    import util from '../../common/js/util'
    import {getExamPaper, userDelete} from "../../api/api";
    export default {
        data(){
            return {
                examPaper: {},
                allPaper: [],
                selectedItems: [],
                page: 1,
                pageSize: 10,
                total: 0,
                accountNumber: sessionStorage.getItem('accountNumber')==null?localStorage.getItem('accountNumber'):sessionStorage.getItem('accountNumber'),
            };
        },
        methods: {
            getAllPaper: function () {
                let para = {
                    teacherId: 0,
                    page: this.page,
                    pageSize: this.pageSize
                };
                getExamPaper(para).then(res=>{
                   this.allPaper = res.list;
                   this.total = res.total;
                });
            },
            selsChange: function () {
                this.selectedItems = items;
            },
            formatExamClass: function (row, column) {
                let ret = new Array();
                for (let i=0; i<row[column.property].length; i++){
                    ret.push(row[column.property][i])
                }
                return ret.join("、")
            },
            showQuestion: function (index, row) {
                this.$router.push({ path: '/assignPaper/?examId=' + row.idStr})
            },
            formatTime: function (row, column) {
                var newDate = new Date();
                newDate.setTime(row[column.property]);
                return util.formatDate.format(newDate, 'yyyy-MM-dd hh:mm:ss');
            },
            deletePaper: function () {
                let para = {
                    ids: this.selectedItems.map(item => item.idStr)
                };
                this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    userDelete(para).then(res => {
                        this.getAllUserInfo();
                    });
                });
            },
//            handleCurrentChange: function (val) {
//                this.page = val;
//                this.getAllPaperAnswer();
//
//            },
        },
        mounted(){
           this.getAllPaper();
        },
    }
</script>
<style>
</style>