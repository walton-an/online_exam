import Login from './views/Login.vue'
import Register from './views/Register.vue'
import NotFound from './views/404.vue'
import studentHome from './views/studentHome.vue'
import teacherHome from './views/teacherHome.vue'
import adminHome from './views/adminHome.vue'
//student
import myExam from './views/student/myExam.vue';
import myPractice from './views/student/myPractice.vue';
import markExam from './views/student/markExam.vue';
import myMark from './views/student/myMark.vue';
import editStudentPSW from './views/student/editStudentPSW.vue';
import editStudentInfo from './views/student/improveStudentInfo.vue';
//teacher
import choiceQuestion from './views/teacher/choiceQuestion.vue';
import blankFillingQuestion from './views/teacher/blankFillingQuestion.vue';
import trueFalseQuestion from './views/teacher/trueFalseQuestion.vue';
import editTeacherPSW from './views/teacher/editTeacherPSW.vue';
import improveTeacherInfo from './views/teacher/improveTeacherInfo.vue';
import assignPaper from './views/teacher/assignPaper.vue';
import paperManager from './views/teacher/paperManager.vue';
import paperAnswer from './views/teacher/paperAnswerManager.vue';
//admin
import addUser from './views/admin/addUser.vue'
import paperAnswerManager from './views/admin/paperAnswerManager.vue'
import userManager from './views/admin/userManager.vue'

let routes = [
    //映射登录
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    //映射注册
    {
        path: '/register',
        component: Register,
        name: '',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    //映射学生用户界面
    {
        path: '/student',
        component: studentHome,
        name: '我的测试',
        iconCls:'fa el-icon-edit',
        isStudent: true,
        leaf: true,//只有一个节点
        children: [
            { path: '/myExam', component: myExam, name: '我的测试' },
        ]
    },
    {
        path: '/student',
        component: studentHome,
        name: '我的练习',
        iconCls:'fa fa-file-o',
        isStudent: true,
        hidden: true,
        children: [
            { path: '/myPractice', component: myPractice, name: '我的练习' },
        ]
    },
    {
        path: '/student',
        component: studentHome,
        name: '交叉评分',
        iconCls:'fa fa-file-o',
        isStudent: true,
        leaf: true,//只有一个节点
        children: [
            { path: '/markExam', component: markExam, name: '交叉评分' },
        ]
    },
    {
        path: '/student',
        component: studentHome,
        name: '我的成绩',
        iconCls:'fa fa-file-text',
        isStudent: true,
        hidden: true,
        children: [
            { path: '/myMark', component: myMark, name: '我的成绩' },
        ]
    },
    {
        path: '/student',
        component: studentHome,
        name: '修改信息',
        iconCls:'fa fa-address-card',
        isStudent: true,
        leaf: true,//只有一个节点
        children: [
            // { path: '/editStudentInfo', component: editStudentInfo,  name: "完善信息"},
            { path: '/editStudentPSW', component: editStudentPSW,  name: "修改密码"}
        ]
    },
    //映射教师用户界面
    {
        path: '/teacher',
        component: teacherHome,
        name: '主观题',
        iconCls: 'fa el-icon-edit',
        isTeacher: true,
        leaf: true,//只有一个节点
        children: [
            { path: '/blankFillingQuestion', component: blankFillingQuestion,  name: "布置题目"},
            { path: '/choiceQuestion', component: choiceQuestion,  name: "选择题", hidden: true},
            { path: '/trueFalseQuestion', component: trueFalseQuestion,  name: "判断题", hidden: true},
            { path: '/assignExam', component: blankFillingQuestion,  name: "主观题", hidden: true},
        ]
    },
    {
        path: '/teacher',
        component: teacherHome,
        name: '答题卡管理',
        iconCls: 'fa fa-file-o',
        isTeacher: true,
        leaf: true,//只有一个节点
        children: [
            { path: '/paperAnswer', component: paperAnswer,  name: "答题卡管理"},
        ]
    },
    {
        path: '/teacher',
        component: teacherHome,
        name: '测试管理',
        iconCls: 'fa fa-file-o',
        isTeacher: true,
        children: [
            { path: '/assignPaper', component: assignPaper,  name: "生成测试"},
            { path: '/paperManager', component: paperManager,  name: "测试管理"}
        ]
    },

    {
        path: '/teacher',
        component: teacherHome,
        name: '信息管理',
        iconCls: 'fa fa-address-card',
        isTeacher: true,
        children: [
            { path: '/improveTeacherInfo', component: improveTeacherInfo,  name: "完善信息"},
            { path: '/editTeacherPSW', component: editTeacherPSW,  name: "修改密码"}
        ]
    },
    //映射管理员界面
    {
        path: '/admin',
        component: adminHome,
        name: '用户管理',
        iconCls:'fa fa-address-card',
        isAdmin: true,
        leaf: true,//只有一个节点
        children: [
            { path: '/userManager', component: userManager, name: '用户管理' },
        ]
    },
    {
        path: '/admin',
        component: adminHome,
        name: '答卷管理',
        iconCls:'fa fa-file-o',
        isAdmin: true,
        leaf: true,//只有一个节点
        children: [
            { path: '/paperAnswerManager', component: paperAnswerManager, name: '答卷管理' },
        ]
    },
    {
        path: '/admin',
        component: adminHome,
        // name: '添加 / 更新用户',
        iconCls:'fa fa-address-card',
        isAdmin: true,
        leaf: true,//只有一个节点
        children: [
            { path: '/addUser', component: addUser, name: '添加 / 更新用户' },
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/login' }
    }
];

export default routes;