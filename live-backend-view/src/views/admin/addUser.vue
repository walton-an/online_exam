<template>
    <el-form :model="userForm" :rules="rules" ref="userForm" label-width="100px" class="demo-ruleForm login-container">
        <h3 class="title"></h3>
        <el-form-item label="学号：" prop="accountNumber">
            <el-input type="number" v-model="userForm.accountNumber" auto-complete="off" placeholder="6-10个数字"></el-input>
        </el-form-item>
        <el-form-item label="姓名：" prop="name">
            <el-input type="text" v-model="userForm.name" auto-complete="off" placeholder="2-8个字符"></el-input>
        </el-form-item>

        <el-form-item label="年级：" prop="grade">
            <el-select clearable v-model="userForm.grade">
                <el-option v-for="item in 2017-1990" :label="(item+1990)" :value="item+1990+''" :key="item+1990+''"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="学院：" prop="school">
            <el-select clearable v-model="userForm.school" @change="getSelectMajor">
                <el-option v-for="item in school" :key="item.idStr" :label="item.title" :value="item.idStr"></el-option>
            </el-select>
            &nbsp&nbsp&nbsp专业：
            <el-select multiple  clearable v-model="userForm.major" :disabled="this.unable" clearable >
                <el-option v-for="item in majorTemp" :key="item.idStr" :label="item.title" :value="item.title" ></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="密码：" prop="password">
            <el-input type="text" v-model="userForm.password" auto-complete="off" placeholder="6-15个字符"></el-input>
        </el-form-item>
        <el-form-item label="选择身份：" prop="type">
            <el-select clearable v-model="userForm.type">
                <el-option label="学生" value="0"></el-option>
                <el-option label="教师" value="1"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item style="width:100%;">
            <el-button type="primary" style="width:100%;" @click="Register" :loading="loading" v-text="buttonName"></el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    import {checkAccountNumber, getInfo, userRegister} from '../../api/api';
    export default {
        data() {
            var g = this.GLOBAL.defaultConfig;
            return {
                loading: false,
                userForm: {
                    accountNumber: '',
                    password: '',
                    type: '',
                    name: '',
                    school: '',
                    major: [],
                    grade: "2017"
                },
                buttonName: "添加",
                school: g.school,
                major: g.major,
                majorTemp: [],
                unable: true,
                rules: {
                    accountNumber: [
                        { required: true, message: '请输入学号', trigger: 'blur' },
//                        { type:"integer", required: true, message: '学号必须为数字', trigger: 'blur' },
                        { min: 6, max: 10, message: '长度在 6 到 10 个数字', trigger: 'blur' },
                        { validator: this.accountNumberValidator, trigger: 'blur' }
                    ],
                    name: [
                        { required: true, message: '请输入姓名', trigger: 'blur' },
                        { min: 2, max: 8, message: '长度在 2 到 8 个字符', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
                    ],
                    type: [
                        { required: true, message: '请选择身份', trigger: 'change' }
                    ],
                    school: [
                        { type:"integer", required: true, message: '请选择学院、专业', trigger: 'change' }
                    ],
                    grade: [
                        { required: true, message: '请选择年级', trigger: 'blur' },
                        { required: true, message: '请选择年级', trigger: 'change' }
                    ],
                },
                checked: true,
                accountNumber: "",
                isEdit: true,
                initNumber: "",
            };
        },
        methods: {
            accountNumberValidator: function (rule, value, callback) {
                if (value == ''){
                    return;
                };
                if( this.userForm.accountNumber == this.initNumber && this.buttonName == "更新"){
                    callback();
                    return;
                }
                let para = {
                    accountNumber: value,
                };
                checkAccountNumber(para).then((res) => {
                    if (res.data === true){
                        callback();
                    }else {
                        callback('该学号已存在!');
                    }
                });
            },
            getSelectMajor: function () {
                this.majorTemp  = this.major.filter((r) => r.parentIdStr === this.userForm.school);
                this.unable = false;
            },
            //用户注册
            Register: function () {
                this.$refs.userForm.validate((valid) => {
                    if (valid) {
                        //获取表单参数
                        let userParas = Object.assign({}, this.userForm);
                        var schoolName = "";
                        for(var i = 0; i< this.school.length;i++){
                            if(this.school[i].idStr===this.userForm.school){
                                schoolName = this.school[i].title;
                                break;
                            }
                        }
                        userParas.school = schoolName;
                        this.loading = true;
                        userRegister(userParas).then((res) => {
                            this.loading = false;
                            console.log(res.data);
                            if (res.data !== undefined) {
                                this.$message({
                                    message: '注册成功',
                                    type: 'success'
                                });
                                this.$router.push({path: '/userManager'});  //login.vue页面
                            }else {
                                this.$message({
                                    message: '用户已存在',
                                    type: 'error'
                                });
                                return false;
                            }
                        })
                    }else {  //没通过表单验证（没输入账号，密码等）
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            initRoom: function () {
                if (!_.isUndefined(this.accountNumber) && !_.isEmpty(this.accountNumber)) {
                    this.isEdit = false;
                    this.buttonName = "更新";
                    this.initNumber = this.accountNumber;
                    let para = {
                        accountNumber: this.accountNumber
                    };
                    getInfo(para).then(res => {
                        this.userForm.accountNumber = res.data.accountNumber + "";
                        this.userForm.name = res.data.name;
                        this.userForm.password = res.data.password;
                        this.userForm.grade = res.data.managerClasses[0].grade + "";
                        this.userForm.type = res.data.type+"";
                        var school = res.data.managerClasses[0].school;
                        for(var i = 0; i< this.school.length; i++){
                            if(this.school[i].title === school){
                                this.userForm.school = this.school[i].idStr;
                                break;
                            }
                        }
                        var major = res.data.managerClasses[0].major;
//                        this.userForm.major = [];
                        this.majorTemp = this.major.filter((r) => r.parentIdStr === this.userForm.school);
                        for(var j =0; j < major.length; j++){
                            for(var i = 0; i< this.majorTemp.length; i++){
                                if(this.majorTemp[i].title.localeCompare(major[j])==0){
                                    this.userForm.major.push(this.majorTemp[i]);
                                    break;
                                }
                            }
                        }
//                        if (!_.isEmpty(this.userForm.major)){
//                            this.userForm.major.forEach(function (val,index,arr) {
//                                arr[index] = val.idStr;
//                            })
//                        }else {
//                            this.userForm.major = [];
//                        }
                    })
                }else {
//                    this.resetForm();
                    this.isEdit = true;
                }
            },
            resetForm: function () {
                this.userForm.accountNumber = "";
                this.userForm.password = "";
                this.userForm.name = "";
                this.userForm.type = "";
                this.userForm.school = "";
                this.userForm.major = "";
                this.userForm.grade = "2017";
                this.unable = true;
                this.buttonName = "添加";
            },
        },
        watch: {
            $route(){
                this.accountNumber = this.$route.query.accountNumber
            },
            accountNumber: function (current, old) {
                this.initRoom();
            },
        },
        mounted(){
            this.accountNumber = this.$route.query.accountNumber;
            this.initRoom();
        },
    }

</script>

<style lang="scss" scoped>
    .login-container {

    }
</style>