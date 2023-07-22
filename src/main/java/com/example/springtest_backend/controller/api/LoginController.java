package com.example.springtest_backend.controller.api;

import com.example.springtest_backend.entity.TokenSubject;
import com.example.springtest_backend.entity.User;
import com.example.springtest_backend.mapper.UserMapper;
import com.example.springtest_backend.response.base.BaseResponse;
import com.example.springtest_backend.utils.Auth;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/api/sign")
@RestController
public class LoginController {

    BaseResponse WRONG_TOKEN = BaseResponse.fatal("Wrong token");

    @Autowired
    UserMapper userMapper;

    // 登录
    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user){
        String password = user.getPassword();
        user = userMapper.getUserByUsername(user.getUsername());
        if (user != null) {
            if (user.getPassword().equals(password)){
                user = userMapper.getUserByUsername(user.getUsername());
                return BaseResponse.ok().addResult("token", Auth.tokenGen(user.getId(), user.isAdmin()));
            }
        }
        return BaseResponse.fatal("Wrong username or password");
    }

    // 注册
    @PostMapping("/register")
    public BaseResponse register(@RequestBody User user){
        if (userMapper.getCountsByUsername(user.getUsername()) > 0 ){ return BaseResponse.fatal("Username existed");}
        System.out.println("user = " + user);
        if (userMapper.addUser(user) == 1){
            user = userMapper.getUserByUsername(user.getUsername());
            return BaseResponse.ok().addResult("token", Auth.tokenGen(user.getId(), user.isAdmin()));
        } else {
            return BaseResponse.error("Something went wrong");
        }
    }

    // 重置密码
    @PutMapping("/resetPassword")
    public BaseResponse resetPassword(@RequestBody Map<String,String> json){
        String username = json.get("username");
        String oldPassword = json.get("oldPassword");
        String newPassword = json.get("newPassword");
        if (username == null || oldPassword == null || newPassword == null || username.equals("") || oldPassword.equals("") || newPassword.equals("")){
            return BaseResponse.fatal("Please provide username, oldPassword and newPassword");
        }
        User newUser = new User();
        newUser.setUsername(username);
        User oldUser = userMapper.getUserByUsername(username);
        if ( oldUser == null ){ return BaseResponse.fatal("User not exist");}
        if (oldPassword.equals(oldUser.getPassword())){
            newUser.setId(oldUser.getId());
            newUser.setPassword(newPassword);
            System.out.println("newUser = " + newUser);
            if (userMapper.updateUsernameById(newUser) > 0){
                return BaseResponse.ok();
            } else {
                return BaseResponse.error("Something went wrong");
            }
        } else {
            return BaseResponse.fatal("Wrong oldPassword");
        }
    }

    // 重置用户名
    @PutMapping("/token/resetUsername")
    public BaseResponse resetUsername(@RequestBody Map<String, String> json, HttpServletRequest request) throws JsonProcessingException {
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims == null){ return WRONG_TOKEN; }
        TokenSubject sub = Auth.getSubFromClaim(claims);
        String username = json.get("username");
        if (username == null || username.equals("")){ return BaseResponse.fatal("Username not provided"); }
        if (userMapper.getCountsByUsername(username) > 0){ return BaseResponse.fatal("Username existed"); }
        User newUser = userMapper.getUserById(sub.getId());
        newUser.setUsername(username);
        if (userMapper.updateUsernameById(newUser) == 0){ return BaseResponse.fatal("Add user fatal");}
        return BaseResponse.ok();
    }

    // 检测token
    @GetMapping("/token/checkToken")
    public BaseResponse checkToken(HttpServletRequest request){
//        String token = request.getHeader("token");
//        Claims claims = Auth.tokenParse(token);
//        String sub = claims.getSubject();
//        System.out.println("sub = " + sub);
//        Object subO = JSONUtils.parse(sub);
//        System.out.println("subO = " + subO);
//        return SignResponse.ok(token);
        return BaseResponse.ok();
    }


}
