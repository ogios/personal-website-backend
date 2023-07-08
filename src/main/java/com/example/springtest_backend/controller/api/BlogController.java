package com.example.springtest_backend.controller.api;


import com.example.springtest_backend.entity.Blog;
import com.example.springtest_backend.entity.TokenSubject;
import com.example.springtest_backend.mapper.BlogMapper;
import com.example.springtest_backend.response.BlogResponse;
import com.example.springtest_backend.utils.Auth;
import com.example.springtest_backend.utils.FileUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.example.springtest_backend.utils.FileUtil.isImage;

@RequestMapping("/api/blogs")
@RestController
public class BlogController {

    BlogResponse WRONG_TOKEN = BlogResponse.fatal(401, "Wrong token");

    @Autowired
    BlogMapper blogMapper;

    @GetMapping("/")
    public BlogResponse getBlogsSlice(@RequestParam int size, @RequestParam int index){
        System.out.println("size = " + size);
        System.out.println("index = " + index);
        List<Blog> blogs = blogMapper.getBlogByPage(size * index, size);
        int total = blogMapper.getCounts();
        System.out.println("blogs = " + blogs);
        return BlogResponse.ok()
                .addData("blogs", blogs)
                .addData("total", total);
    }

    @GetMapping("/blog")
    public BlogResponse getBlogById(@RequestParam int id, HttpServletRequest request) throws IOException {
        Blog blog = blogMapper.getBlogById(id);
        if (blog != null){
            try{
                blog.setContentRaw(FileUtil.getTextByName(
                        request.getServletContext().getRealPath("/"),
                        blog.getContent()
                ));
                return BlogResponse.ok()
                        .addData("info", blog);
            } catch (FileNotFoundException e){
                return BlogResponse.fatal(404, "Content not found");
            }
        } else {
            return BlogResponse.fatal(404, "Blog not exist");
        }
    }

    @PostMapping("/token/imageUpload")
    public BlogResponse imageUpload(MultipartFile file, HttpServletRequest request) throws IOException {
        // 从token获取sub
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims == null){ return WRONG_TOKEN; }
        TokenSubject sub = Auth.getSubFromClaim(claims);

        // 文件检测
        if (file == null) {return BlogResponse.fatal(400, "No file provided"); }
        boolean is_img = isImage(file);
        System.out.println("is_img = " + is_img);
        if (!is_img){ return BlogResponse.fatal(400, "Only support image file"); }

        // 获取路径并保存 | 保存后返回文件名并以虚拟路径发送
        String path = FileUtil.getImageFilePath(sub.getId(), request.getServletContext().getRealPath("/"), file.getOriginalFilename());
        System.out.println("path = " + path);
        String name = FileUtil.saveFile(file, path);
        BlogResponse blogResponse = BlogResponse.ok();
        blogResponse.addData("name", name);
        return blogResponse;
    }

    @PostMapping("/token/blogUpload")
    public BlogResponse blogUpload(@RequestBody Blog blog, HttpServletRequest request) throws IOException {
        // 从token获取sub
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims ==null){ return WRONG_TOKEN; }
        TokenSubject sub = Auth.getSubFromClaim(claims);
        int id = sub.getId();

        // 检测内容
        if (blog.getContentRaw() == null || blog.getContentRaw().equals("")){
            return BlogResponse.fatal(400, "No content provided.");
        }

        // 保存文件
        String path = FileUtil.getStringFilePath(sub.getId(), request.getServletContext().getRealPath("/"));
        String name = FileUtil.saveFile(blog.getContentRaw(), path);

        // 设置对象并insert
        blog.setContent(name);
        blog.setOwnerId(sub.getId());
        blog.setUpdateUserId(blog.getOwnerId());
        if (blog.getCreateTime() == null || blog.getCreateTime().equals("")){
            blog.setCreateTime(new Date().toString());
        }
        blog.setUpdateTime(blog.getCreateTime());
        System.out.println("blog.toString() = " + blog);
        if (blogMapper.insertOneBlog(blog) > 0){
            if (blogMapper.getUserToBlogCountByIds(blog.getId(), blog.getOwnerId()).size() == 0){
                blogMapper.addUserToBlog(blog.getId(), blog.getOwnerId());
            }
            return BlogResponse.ok().addData("id", blog.getId());
        } else {
            return BlogResponse.error(400, "Something went wrong.");
        }
    }

    @PostMapping("/token/blogUpdate")
    public BlogResponse blogUpdate(@RequestBody Blog blog, HttpServletRequest request) throws IOException {
        // 从token获取sub
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims ==null){ return WRONG_TOKEN; }
        TokenSubject sub = Auth.getSubFromClaim(claims);
        int id = sub.getId();

        // 检测内容与权限
        if (blog.getContentRaw() == null || blog.getContentRaw().equals("")){
            return BlogResponse.fatal(400, "No content provided.");
        }
        if (blogMapper.getUserToBlogCountByIds(blog.getId(), id).size() == 0){
            return BlogResponse.fatal(401, "No authorization's granted.");
        }

        // 保存文件
        String path = FileUtil.getStringFilePath(request.getServletContext().getRealPath("/"), blogMapper.getBlogById(blog.getId()).getContent());
        String name = FileUtil.saveFile(blog.getContentRaw(), path);

        // 设置对象并update
        blog.setContent(name);
        blog.setUpdateUserId(sub.getId());
        if (blog.getUpdateTime() == null || blog.getUpdateTime().equals("")){
            blog.setUpdateTime(new Date().toString());
        }
        if (blogMapper.updateBlogById(blog) > 0){
            return BlogResponse.ok().addData("id", blog.getId());
        } else {
            return BlogResponse.error(400, "Something went wrong.");
        }
    }







}