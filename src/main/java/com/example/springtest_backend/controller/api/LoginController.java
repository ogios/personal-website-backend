package com.example.springtest_backend.controller.api;

import com.alibaba.druid.support.json.JSONUtils;
import com.example.springtest_backend.entity.TokenSubject;
import com.example.springtest_backend.entity.User;
import com.example.springtest_backend.mapper.UserMapper;
import com.example.springtest_backend.response.SignResponse;
import com.example.springtest_backend.utils.Auth;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/sign")
@RestController
public class LoginController {

    SignResponse WRONG_TOKEN = SignResponse.fatal(401, "Wrong token");

    @Autowired
    UserMapper userMapper;

    // 登录
    @PostMapping("/login")
    public SignResponse login(@RequestBody User user){
        String password = user.getPassword();
        user = userMapper.getUserByUsername(user.getUsername());
        if (user != null) {
            if (user.getPassword().equals(password)){
                user = userMapper.getUserByUsername(user.getUsername());
                return SignResponse.ok(Auth.tokenGen(user.getId(), user.isAdmin()));
            }
        }
        return SignResponse.fatal(400, "Wrong username or password");
    }

    // 注册
    @PostMapping("/register")
    public SignResponse register(@RequestBody User user){
        if (userMapper.getCountsByUsername(user.getUsername()) > 0 ){ return SignResponse.fatal(400, "Username existed");}
        System.out.println("user = " + user);
        if (userMapper.addUser(user) == 1){
            user = userMapper.getUserByUsername(user.getUsername());
            return SignResponse.ok(Auth.tokenGen(user.getId(), user.isAdmin()));
        } else {
            return SignResponse.error("Something went wrong");
        }
    }

    // 重置密码
    @PutMapping("/resetPassword")
    public SignResponse resetPassword(@RequestBody String username, @RequestBody String oldPassword, @RequestBody String newPassword, HttpServletRequest request){
        User newUser = new User();
        newUser.setUsername(username);
        User oldUser = userMapper.getUserByUsername(username);
        if ( oldUser == null ){ return SignResponse.fatal(404, "User not exist");}
        if (oldPassword.equals(oldUser.getPassword())){
            newUser.setPassword(newPassword);
        }
        System.out.println("newUser = " + newUser);

        if (userMapper.updateUsernameById(newUser) > 0){
            return SignResponse.ok("");
        } else {
            return SignResponse.error("Something went wrong");
        }
    }

    // 重置用户名
    @PutMapping("/token/resetUsername")
    public SignResponse resetUsername(@RequestBody String username, HttpServletRequest request) throws JsonProcessingException {
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims == null){ return WRONG_TOKEN; }
        TokenSubject sub = Auth.getSubFromClaim(claims);
        if (userMapper.getCountsByUsername(username) > 0){ return SignResponse.fatal(400, "Username existed"); }
        User newUser = userMapper.getUserById(sub.getId());
        newUser.setUsername(username);
        if (userMapper.updateUsernameById(newUser) == 0){ return SignResponse.fatal(500, "Add user fatal");}
        return SignResponse.ok("");
    }

    // 检测token
    @GetMapping("/token/checkToken")
    public SignResponse checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        Claims claims = Auth.tokenParse(token);
        String sub = claims.getSubject();
        System.out.println("sub = " + sub);
        Object subO = JSONUtils.parse(sub);
        System.out.println("subO = " + subO);
        return SignResponse.ok(token);
    }


}
