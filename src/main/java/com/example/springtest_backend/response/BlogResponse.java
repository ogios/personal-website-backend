package com.example.springtest_backend.response;

import java.util.HashMap;
import java.util.Map;

public class BlogResponse {
    private boolean success;
    private int code;
    private String msg;
    private Map<String, Object> data = new HashMap<>();

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

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public BlogResponse addData(String key, Object data){
        this.data.put(key, data);
        return this;
    }

    private BlogResponse(){}

    public static BlogResponse ok(){
        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setCode(200);
        blogResponse.setSuccess(true);
        blogResponse.setMsg("success");
        return blogResponse;
    }

    public static BlogResponse fatal(int code, String msg){
        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setCode(code);
        blogResponse.setSuccess(false);
        blogResponse.setMsg(msg);
        return blogResponse;
    }

    public static BlogResponse error(int code, String msg){
        BlogResponse blogResponse = new BlogResponse();
        blogResponse.setCode(code);
        blogResponse.setSuccess(false);
        blogResponse.setMsg(msg);
        return blogResponse;
    }


}

