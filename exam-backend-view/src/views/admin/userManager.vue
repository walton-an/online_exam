<template>
    <section>
        <el-col :span="24" class="toolbar">
            <el-form :inline="true" :model="filters">
                <el-form-item label="">
                    <el-select v-model="filters.type" placeholder="请选择要搜索的分类" clearable>
                        <el-option
                                v-for="item in type"
                                :key="item.idStr"
                                :value="item.idStr"
                                :label="item.title"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-input v-model="filters.searchTitle" placeholder="输入搜索的名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button icon="search" @click="searchList">搜索</el-button>
                </el-form-item>
            </el-form>
        </el-col>

        <el-table :data="allUser" highlight-current-row v-loading="listLoading" @selection-change="selectChange" style="width: 100%;" ref="userRef">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="accountNumber" label="学号" width="120">
                <template scope="scope">
                    <div class="moreContent" style="color: blue" @click="editUser(scope.$index, scope.row)">
                        {{ scope.row.accountNumber}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="100"></el-table-column>
            <el-table-column prop="type" label="身份" width="80" :formatter="formatType"></el-table-column>
            <el-table-column prop="group" label="组号" width="80" ></el-table-column>
            <el-table-column prop="status" label="状态" width="80" :formatter="formatStatus"></el-table-column>
            <el-table-column prop="managerClasses" label="班级" width="400" :formatter="formatClass"></el-table-column>
            <el-table-column prop="createTime" label="注册时间" :formatter="formatTime" width="180"></el-table-column>
            <el-table-column prop="updateTime" label="更新时间" :formatter="formatTime" width="180"></el-table-column>
            <el-table-column label="设为" width="80">
                <template scope="scope">
                    <div v-if="scope.row.status === 0" style="color:blue;cursor: pointer" @click="updateStatus(scope.$index, scope.row,1)">显示</div>
                    <div v-if="scope.row.status === 1" style="color:red;cursor: pointer" @click="updateStatus(scope.$index, scope.row,0)">隐藏</div>
                </template>
            </el-table-column>
        </el-table>
        <el-col :span="24" class="toolbar">
            <el-form :inline="true"  v-model="operations">
                <el-form-item>
                    <el-button type="danger" @click="deleteUsers" :disabled="this.selectedItems.length===0">删除</el-button>
                </el-form-item>
                <el-pagination layout="total,prev, pager, next" @current-change="handleCurrentChange" :page-size="pageSize" :total="total" style="float:right;"></el-pagination>
            </el-form>
        </el-col>
    </section>
</template>
<script>
    import util from '../../common/js/util';
    import {getAllInfo, searchUser, userDelete, userUpdateStatus} from "../../api/api";
    export default {
        data(){
            var g = this.GLOBAL.defaultConfig;
            return {
                page: 1,
                pageSize: 10,
                total: 0,
                selectedItems: "",
                filters: {
                    type: "",
                    searchTitle: ""
                },
                operations: {
                    oType: "1",
                    status: "",
                },
                status: g.operations,
                type: [
                    {idStr: "0", title: "学号"},
                    {idStr: "1", title: "姓名"},
                    {idStr: "2", title: "班级"}
                ],
                gameStatus: g.gameStatus,
                roomStatus: g.roomStatus,
                rooms: [
                ],
                listLoading: false,
                selectedItems: [],
                allUser: [],
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
                for (let i=0; i<row.managerClasses.length; i++){
                    for (let j =0; j< row.managerClasses[i].major.length; j++){
                        var classes = row.managerClasses[i].grade + row.managerClasses[i].school + row.managerClasses[i].major[j];
                        ret.push(classes);
                    }
                }
                return ret.join("、") ;
            },
            formatTime: function (row, column) {
                var newDate = new Date();
                newDate.setTime(row[column.property]);
                return util.formatDate.format(newDate, 'yyyy-MM-dd hh:mm:ss');
            },
            deleteUsers: function () {
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
            getAllUserInfo: function(){
                let para = {
                    searchType: 4,
                    page: this.page,
                    pageSize: this.pageSize
                };
                searchUser(para).then(res=>{
                    this.allUser = res.data.list;
                    this.total = res.data.size;
                })
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
                this.getAllUserInfo();
            },
        },
        mounted(){
          this.getAllUserInfo();
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