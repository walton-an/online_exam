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
     * Paper
     */
    String PAPER_BASE_ROUTE = "/paper";
    String PAPER_ADD_QUESTION = "/addQuestion";
    String PAPER_GET_QUESTIOND_SIZE = "/getQuestionSize";
    String PAPER_GET_QUESTIOND_LIST = "/getQuestionList";
    String PAPER_SEARCH_QUESTION = "/searchQuestion";
    String PAPER_UPDATE_QUESTION = "/updateQuestion";
    String PAPER_DELETE_QUESTION = "/deleteQuestion";

    /**
     * Exam
     */
    String EXAM_BASE_ROUTE = "/exam";
    String EXAM_CREATE_UPDATE = "/createOrUpdatePaper";
    String EXAM_GET = "/getPaper";
    String EXAM_GET_PAPEER_BY_ID = "/getPaperById";
    String EXAM_ADD_PAPER_ANSWER = "/addPaperAnswer";
    String EXAM_EXIST_PAPER_ANSWER = "/existPaperAnswer";
}
