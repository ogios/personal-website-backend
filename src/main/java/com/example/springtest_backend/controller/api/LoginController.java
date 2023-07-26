package com.example.springtest_backend.controller.api;

import com.example.springtest_backend.entity.TokenSubject;
import com.example.springtest_backend.entity.User;
import com.example.springtest_backend.mapper.UserMapper;
import com.example.springtest_backend.response.base.BaseResponse;
import com.example.springtest_backend.utils.Auth;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/sign")
@RestController
public class LoginController {

    BaseResponse WRONG_TOKEN = BaseResponse.fatal("Wrong token");

    @Autowired
    UserMapper userMapper;

    @Autowired
    ObjectMapper objectMapper;

    // 登录
    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user){
//        user = userMapper.getUserByUsername(user.getUsername());
        List<User> users = userMapper.getUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (users.size() == 1) {
            user = users.get(0);
            return BaseResponse.ok().addResult("token", Auth.tokenGen(user.getId(), user.getIsAdmin()));
        }
        return BaseResponse.fatal("Wrong username or password");
    }

//    // 注册
//    @PostMapping("/register")
//    public BaseResponse register(@RequestBody User user){
//        if (userMapper.getCountsByUsername(user.getUsername()) > 0 ){ return BaseResponse.fatal("Username existed");}
//        System.out.println("user = " + user);
//        if (userMapper.addUser(user) == 1){
//            user = userMapper.getUserByUsername(user.getUsername());
//            return BaseResponse.ok().addResult("token", Auth.tokenGen(user.getId(), user.getIsAdmin()));
//        } else {
//            return BaseResponse.error("Something went wrong");
//        }
//    }
//
//    // 重置密码
//    @PutMapping("/resetPassword")
//    public BaseResponse resetPassword(@RequestBody Map<String,String> json){
//        String username = json.get("username");
//        String oldPassword = json.get("oldPassword");
//        String newPassword = json.get("newPassword");
//        if (username == null || oldPassword == null || newPassword == null || username.equals("") || oldPassword.equals("") || newPassword.equals("")){
//            return BaseResponse.fatal("Please provide username, oldPassword and newPassword");
//        }
//        User newUser = new User();
//        newUser.setUsername(username);
//        User oldUser = userMapper.getUserByUsername(username);
//        if ( oldUser == null ){ return BaseResponse.fatal("User not exist");}
//        if (oldPassword.equals(oldUser.getPassword())){
//            newUser.setId(oldUser.getId());
//            newUser.setPassword(newPassword);
//            System.out.println("newUser = " + newUser);
//            if (userMapper.updateUsernameById(newUser) > 0){
//                return BaseResponse.ok();
//            } else {
//                return BaseResponse.error("Something went wrong");
//            }
//        } else {
//            return BaseResponse.fatal("Wrong oldPassword");
//        }
//    }
//
//    // 重置用户名
//    @PutMapping("/token/resetUsername")
//    public BaseResponse resetUsername(@RequestBody Map<String, String> json, HttpServletRequest request) throws JsonProcessingException {
//        Claims claims = Auth.getClaimFromRequest(request);
//        if (claims == null){ return WRONG_TOKEN; }
//        TokenSubject sub = Auth.getSubFromClaim(claims);
//        String username = json.get("username");
//        if (username == null || username.equals("")){ return BaseResponse.fatal("Username not provided"); }
//        if (userMapper.getCountsByUsername(username) > 0){ return BaseResponse.fatal("Username existed"); }
//        User newUser = userMapper.getUserById(sub.getId());
//        newUser.setUsername(username);
//        if (userMapper.updateUsernameById(newUser) == 0){ return BaseResponse.fatal("Add user fatal");}
//        return BaseResponse.ok();
//    }

    // 检测token
    @GetMapping("/token/checkToken")
    public BaseResponse checkToken(HttpServletRequest request) throws JsonProcessingException {
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims == null){ return WRONG_TOKEN; }
        TokenSubject sub = Auth.getSubFromClaim(claims);
        int id = sub.getId();
        if (userMapper.getCountsById(id) == 1)
            return BaseResponse.ok();
        else return BaseResponse.fatal("Wrong user");
    }

    @GetMapping("/token/userinfo")
    public BaseResponse getUserInfo(HttpServletRequest request) throws JsonProcessingException, ParseException {
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims == null){ return WRONG_TOKEN; }
        TokenSubject sub = Auth.getSubFromClaim(claims);
        int id = sub.getId();
//        ArrayList<Object> tabs = new JSONParser("[\"test4\", \"test2\", \"test1\"]").list();
//        System.out.println("tabs = " + tabs);
        User user = userMapper.getUserById(id);
        if (user != null){
            user.setPassword(null);
            String test = objectMapper.writeValueAsString(user);
            System.out.println("test = " + test);
            return BaseResponse.ok().addResult("userinfo", user);
        } else {
            return BaseResponse.fatal("user not exist");
        }
    }



}
