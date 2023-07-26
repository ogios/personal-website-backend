package com.example.springtest_backend.mapper;

import com.example.springtest_backend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    // select
    // 用户名获取用户信息
    @Select("SELECT * FROM t_user WHERE username=#{username}")
    User getUserByUsername(String username);

    @Select("SELECT * FROM t_user WHERE username=#{username} AND password=MD5(#{password})")
    List<User> getUserByUsernameAndPassword(String username, String password);

    // id获取用户信息
    @Select("SELECT * FROM t_user WHERE id=#{id}")
    User getUserById(int id);

    // 获取指定用户名下用户数量 (用于判断用户是否存在)
    @Select("SELECT count(id) FROM t_user WHERE username=#{username}")
    int getCountsByUsername(String username);

    @Select("SELECT count(id) FROM t_user WHERE id=#{id}")
    int getCountsById(int id);

    // 获取全部用户数量
    @Select("SELECT count(id) FROM t_user")
    int getCounts();


    // insert
    // 添加新用户| 用户名-密码
    @Insert("INSERT INTO t_user (username, password) VALUES(#{username},MD5(#{password}))")
    int addUser(User user);


    // update
    // 更新用户名
    @Update("UPDATE t_user SET username=#{username},password=#{password} WHERE id=#{id}")
    int updateUsernameById(User user);

    // 更新用户密码

}
