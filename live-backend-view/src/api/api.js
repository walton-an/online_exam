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
export const getAllInfo = params => {
    return axios.get(`${base}user/getAllInfo`,{params:params}).then(res=>res.data);
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

/******* paper *******/
export const addQuestion = params => {
    return axios.post(`${base}paper/addQuestion`, params).then(res=>res.data);
};
export const getQuestionSize = params => {
    return axios.get(`${base}paper/getQuestionSize`,{params:params}).then(res=>res.data);
};
export const getQuestionList = params => {
    return axios.get(`${base}paper/getQuestionList`, {params:params} ).then(res=>res.data);
};
export const searchQuestion = params => {
    return axios.get(`${base}paper/searchQuestion`, {params:params} ).then(res=>res.data);
};
export const updateQuestion = params => {
    return axios.post(`${base}paper/updateQuestion`, params).then(res=>res.data);
};
export const deleteQuestion = params => {
    return axios.post(`${base}paper/deleteQuestion`, params).then(res=>res.data);
};


/******* exam *******/
export const createOrUpdatePaper = params => {
    return axios.post(`${base}exam/createOrUpdatePaper`, params).then(res=>res.data);
};
export const getExamPaper = params => {
    return axios.get(`${base}exam/getPaper`, {params:params}).then(res=>res.data);
};
export const getExamPaperById = params => {
    return axios.get(`${base}exam/getPaperById`, {params:params}).then(res=>res.data);
};
export const addPaperAnswer = params => {
    return axios.post(`${base}exam/addPaperAnswer`, params).then(res=>res.data);
};
export const existPaperAnswer = params => {
    return axios.get(`${base}exam/existPaperAnswer`, {params:params}).then(res=>res.data);
};