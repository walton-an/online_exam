<template>
    <section>

        <!--修改信息-->
        <el-col>
            <el-form ref="studentInfo"  :rules="studentInfoRules" :model="studentInfo"  label-width="100px" class="demo-ruleForm">
                <el-form-item label="学号：" >
                    <el-tag type="info">{{studentInfo.accountNumber}}</el-tag>
                </el-form-item>
                <el-form-item label="姓名：" >
                    <el-tag type="info">{{studentInfo.name}}</el-tag>
                </el-form-item>
                <el-form-item label="班级：" >
                    <el-tag type="info" v-for="grade1 in studentInfo.managerClasses">{{grade1.grade}}届{{grade1.school}}{{grade1.major}}</el-tag>
                </el-form-item>
                <el-form-item prop="newPassword" label="新密码：">
                    <el-input type="password" v-model="studentInfo.newPassword" auto-complete="off" placeholder="请输入新密码 (1-15个字符)"></el-input>
                </el-form-item>
                <el-form-item prop="checknewPassword" label="确认密码：">
                    <el-input type="password" v-model="studentInfo.checknewPassword" auto-complete="off" placeholder="请再次输入新密码"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="updateInfo()">立即修改</el-button>
                    <el-button @click="resetForm('studentInfo')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-col>

    </section>
</template>

<script>
    import {getInfo, updateInfo} from '../../api/api'
    import Util from "../../common/js/util";
    export default {
        data(){
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.studentInfo.checknewPassword !== '') {
                        this.$refs.studentInfo.validateField('checknewPassword');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.studentInfo.newPassword) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                studentInfo: {
                    accountNumber: '',
                    name: '',
                    password: '',
                    newPassword: '',
                    checknewPassword: '',
                    school: '',
                    grade: '',
                },
                accountNumber: sessionStorage.getItem('accountNumber'),
                name: sessionStorage.getItem('name'),
                password: '',
                newPassword: '',
                checknewPassword: '',
                school: '',
                grade: '',
                studentInfoRules: {
                    newPassword: [
                        { required: true, message: '请输入新密码', trigger: 'blur' },
                        { min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur' },
                        { validator: validatePass, trigger: 'blur' }
                    ],
                    checknewPassword: [
                        { required: true, message: '请再次输入新密码', trigger: 'blur' },
                        { min: 1, max: 15, message: '长度在 1 到 15 个字符', trigger: 'blur' },
                        { validator: validatePass2, trigger: 'blur' }
                    ],
                }
            };
        },
        methods: {
            //判断是否操作成功
            checkResult: function (res, noMessage, message) {
                if (res.code == "1"){
                    if (noMessage){
                        this.$message({
                            message: message?message:"操作成功!",
                            type: "success"
                        });
                    }
                    return true;
                }else {
                    this.$message({
                        message: "操作失败,失败信息为:" + res.desc,
                        type: "error"
                    })
                }
                return false;
            },
            updateInfo: function () {
                this.$refs.studentInfo.validate((valid) => {
                    if (valid) {
                        this.$confirm('确认提交吗？', '提示', {}).then(() => {
                            let para = {
                                accountNumber: this.accountNumber,
                                password: this.studentInfo.newPassword,
                            };
                            updateInfo(para).then((res) => {
                                if (res.data !== undefined) {
                                    this.$message({
                                        message: '更新成功',
                                        type: 'success'
                                    });
                                    this.resetForm('studentInfo');
                                }else{
                                    this.$message({
                                        message: '更新失败',
                                        type: 'error'
                                    });
                                }
                                this.$router.push({path: '/editStudentPSW'});
                            });
                        });
                    }
                });
            },
            //设置评论状态
            setStatus: function () {
                let para = {
                    status: this.updateStatus,
                    ids: this.sels.map(item => item.idStr).toString(),
                };
                this.listLoading = true;
                roomUpdateSelectedComment(para).then(()=>{
                    this.listLoading = false;
                    this.getCommentData();
                });
            },
            //格式化审核进度
            formatCommtentCheck: function(row, column) {
                return row[column.property] == 0 ? '待审核' : '已审核';
            },
            //重置
            resetForm(formName) {
                this.$refs[formName].resetFields();
                this.studentInfo.school = '';
                this.studentInfo.grade = '';
            },
            getStudentInfo: function () {
                let para = {
                    accountNumber: this.accountNumber
                };
                getInfo(para).then(res=>{
                    this.studentInfo = res.data;
                })
            },
        },
        //初始化操作
        mounted(){
            this.getStudentInfo();
        },
    }
</script>

<style lang="scss">

    .tableImg img {
        width: 80px;
        height: 40px;
    }

    .teamUpload .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .teamUpload .avatar-uploader .el-upload:hover {
        border-color: #20a0ff;
    }
    .teamUpload .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 100px;
        height: 100px;
        line-height: 100px;
        text-align: center;
    }
    .teamUpload .avatar {
        width: 100px;
        height: 100px;
        display: block;
    }
    .teamUpload ul {
        padding:0;
        margin:0;
    }
</style>