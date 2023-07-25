package com.example.springtest_backend.utils;

//import com.alibaba.druid.support.json.JSONParser;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import java.util.List;

public class Common {
    public static List<Object> JSONToList(String json) throws ParseException {
        return new JSONParser(json).list();
    }
}
