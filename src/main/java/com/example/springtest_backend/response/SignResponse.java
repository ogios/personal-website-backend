package com.example.springtest_backend.response;

public class SignResponse {
    private boolean success;
    private int code;
    private String msg;
    private String token;

    public static SignResponse ok(String token){
        SignResponse response = new SignResponse();
        response.setCode(200);
        response.setMsg("ok");
        response.setSuccess(true);
        response.setToken(token);
        return response;
    }

    public static SignResponse fatal(int code, String msg){
        SignResponse response = new SignResponse();
        response.setCode(code);
        response.setSuccess(false);
        response.setMsg(msg);
        response.setToken("");
        return response;
    }

    public static SignResponse error(String msg){
        SignResponse response = new SignResponse();
        response.setCode(500);
        response.setSuccess(false);
        response.setMsg(msg);
        response.setToken("");
        return response;
    }

    private SignResponse(){}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
