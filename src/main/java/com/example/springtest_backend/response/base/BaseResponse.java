package com.example.springtest_backend.response.base;

import java.util.HashMap;
import java.util.Map;

public class BaseResponse {

    //    private boolean success;
    private int status;
    private String message;
    private Map<String, Object> result = new HashMap<>();

    public static BaseResponse ok(){
        BaseResponse response = new BaseResponse();
        response.setStatus(200);
        response.setMessage("");
        return response;
    }

    public static BaseResponse fatal(String message){
        BaseResponse response = new BaseResponse();
        response.setStatus(400);
        response.setMessage(message);
        return response;
    }

    public static BaseResponse error(String message){
        BaseResponse response = new BaseResponse();
        response.setStatus(500);
        response.setMessage(message);
        return response;
    }

    public BaseResponse addResult(String key, Object val){
        this.result.put(key, val);
        return this;
    }



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
