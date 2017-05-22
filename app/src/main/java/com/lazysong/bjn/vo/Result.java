package com.lazysong.bjn.vo;

/**
 * 返回信息.
 * Created by 丞 on 2016/9/23.
 */
public class Result {

    private static final String TITLE_SUCCESS = "Success";
    private static final String TITLE_FAILURE = "Error";

    private String title;
    private String result;

    public Result(){}

    private Result(String title, String result) {
        this.title = title;
        this.result = result;
    }

    public static Result getSuccessInstance(String message) {
        return new Result(TITLE_SUCCESS, message);
    }

    public static Result getFailureInstance(String message) {
        return new Result(TITLE_FAILURE, message);
    }
    
    public static Result getFailureTitleInstance(String title, String message) {
        return new Result(title, message);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return this.title.equals(TITLE_SUCCESS);
    }
}
