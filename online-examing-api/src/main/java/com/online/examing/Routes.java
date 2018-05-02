package com.online.examing;

public interface Routes {


    /**
     * User
     */
    String USER_BASE_ROUTE = "/user";
    String USER_LOGIN = "/login";
    String USER_REGISTER = "/register";
    String USER_UPDATE_INFO = "/updateInfo";
    String USER_GET_INFO = "/getInfo";
    String USER_SEARCH = "/search";
    String USER_GET_PAPER = "/getPaper";
    String USER_DELETE = "/delete";
    String USER_UPDATE_STATUS= "/updateStatus";
    String USER_CHECK_NUMBER = "/checkExist";

    /**
     * Question
     */
    String QUESTION_BASE_ROUTE = "/question";
    String ADD_QUESTION = "/addQuestion";
    String GET_QUESTIOND_SIZE = "/getQuestionSize";
    String GET_QUESTIOND_LIST = "/getQuestionList";
    String SEARCH_QUESTION = "/searchQuestion";
    String UPDATE_QUESTION = "/updateQuestion";
    String DELETE_QUESTION = "/deleteQuestion";

    /**
     * Paper
     */
    String PAPER_BASE_ROUTE = "/paper";
    String CREATE_UPDATE_PAPER = "/createOrUpdatePaper";
    String GET_PAPER = "/getPaper";
    String GET_ALL_PAPER = "getAllExamPaper";
    String GET_PAPEER_BY_ID = "/getPaperById";
    String ADD_PAPER_ANSWER = "/addPaperAnswer";
    String ADD_PAPER_SCORE = "/addPaperScore";
    String DELETE_PAPER_ANSWER = "/deletePaperAnswer";
    String GET_PAPER_ANSWER = "/getPaperAnswer";
    String GET_PAPER_ANSWER_DETAIL = "getPaperAnswerDetail";
    String GET_GROUP_PAPER_ANSWER = "/getGroupPaperAnswer";
    String EXIST_PAPER_ANSWER = "/existPaperAnswer";
}
