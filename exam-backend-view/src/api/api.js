import axios from 'axios';
let base = "http://localhost:8081/";
// let base = "http://39.108.144.48:8081/";

/******* user *******/
export const userLogin = params => {
    return axios.post(`${base}user/login`, params).then(res=>res.data);
};
export const userRegister = params => {
    return axios.post(`${base}user/register`, params).then(res=>res.data);
};
export const userDelete = params => {
    return axios.post(`${base}user/delete`, params).then(res=>res.data);
};
export const userUpdateStatus = params => {
    return axios.post(`${base}user/updateStatus`, params).then(res=>res.data);
};
export const updateInfo = params => {
    return axios.post(`${base}user/updateInfo`, params).then(res=>res.data);
};
export const searchUser = params => {
    return axios.get(`${base}user/search`,{params:params}).then(res=>res.data);
};
export const getInfo = params => {
    return axios.get(`${base}user/getInfo`,{params:params}).then(res=>res.data);
};
export const getPaper = params => {
    return axios.get(`${base}user/getPaper`,{params:params}).then(res=>res.data);
};
export const checkAccountNumber = params => {
    return axios.get(`${base}user/checkExist`,{params:params}).then(res=>res.data);
};
// export const searchUser = params => {
//     return axios.get(`${base}user/search`,{params:params}).then(res=>res.data);
// };

/******* question *******/
export const addQuestion = params => {
    return axios.post(`${base}question/addQuestion`, params).then(res=>res.data);
};
export const getQuestionSize = params => {
    return axios.get(`${base}question/getQuestionSize`,{params:params}).then(res=>res.data);
};
export const getQuestionList = params => {
    return axios.get(`${base}question/getQuestionList`, {params:params} ).then(res=>res.data);
};
export const searchQuestion = params => {
    return axios.get(`${base}question/searchQuestion`, {params:params} ).then(res=>res.data);
};
export const updateQuestion = params => {
    return axios.post(`${base}question/updateQuestion`, params).then(res=>res.data);
};
export const deleteQuestion = params => {
    return axios.post(`${base}question/deleteQuestion`, params).then(res=>res.data);
};


/******* paper *******/
export const createOrUpdatePaper = params => {
    return axios.post(`${base}paper/createOrUpdatePaper`, params).then(res=>res.data);
};
export const getExamPaper = params => {
    return axios.get(`${base}paper/getPaper`, {params:params}).then(res=>res.data);
};
export const getAllExamPaper = () => {
    return axios.get(`${base}paper/getAllExamPaper`).then(res=>res.data);
};
export const getExamPaperById = params => {
    return axios.get(`${base}paper/getPaperById`, {params:params}).then(res=>res.data);
};
export const addPaperAnswer = params => {
    return axios.post(`${base}paper/addPaperAnswer`, params).then(res=>res.data);
};
export const addPaperScore = params => {
    return axios.post(`${base}paper/addPaperScore`, params).then(res=>res.data);
};
export const deletePaperAnswer= params => {
    return axios.post(`${base}paper/deletePaperAnswer`, params).then(res=>res.data);
};
export const existPaperAnswer = params => {
    return axios.get(`${base}paper/existPaperAnswer`, {params:params}).then(res=>res.data);
};
export const getPaperAnswer = params => {
    return axios.get(`${base}paper/getPaperAnswer`, {params:params}).then(res=>res.data);
};
export const getGroupPaperAnswer = params => {
    return axios.get(`${base}paper/getGroupPaperAnswer`, {params:params}).then(res=>res.data);
};
export const getPaperAnswerDetail = params => {
    return axios.get(`${base}paper/getPaperAnswerDetail`, {params:params}).then(res=>res.data);
};

