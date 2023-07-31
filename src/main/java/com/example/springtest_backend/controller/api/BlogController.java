package com.example.springtest_backend.controller.api;


import com.example.springtest_backend.entity.Blog;
import com.example.springtest_backend.entity.TokenSubject;
import com.example.springtest_backend.mapper.BlogMapper;
import com.example.springtest_backend.mapper.UserMapper;
import com.example.springtest_backend.response.base.BaseResponse;
import com.example.springtest_backend.utils.Auth;
import com.example.springtest_backend.utils.FileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.springtest_backend.utils.FileUtil.isHTMLExist;
import static com.example.springtest_backend.utils.FileUtil.isImage;

@RequestMapping("/api/blogs")
@RestController
public class BlogController {

    BaseResponse WRONG_TOKEN = BaseResponse.fatal("Wrong token");

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public BaseResponse getBlogsSlice(@RequestParam int size, @RequestParam int pageIndex){
        System.out.println("size = " + size);
        System.out.println("pageIndex = " + pageIndex);
        List<Blog> blogs = blogMapper.getBlogByPage(size * pageIndex, size);
        int total = blogMapper.getCounts();
        System.out.println("blogs = " + blogs);

        return BaseResponse.ok()
                .addResult("blogs", blogs)
                .addResult("total", total)
                .addResult("pageIndex", pageIndex)
                .addResult("size", size);
    }

    @GetMapping("/blog/{id}")
    public BaseResponse getBlogById(@PathVariable("id") int id, HttpServletRequest request) throws ParseException {
        Blog blog = blogMapper.getBlogById(id);
        if (blog != null){
            blog.setTabs(new JSONParser((String) blog.getTabs()).list());
            return BaseResponse.ok().addResult("blogInfo", blog);
        } else {
            return BaseResponse.fatal("Blog not exist");
        }
    }

    @GetMapping("/blog")
    public BaseResponse getBlogByConditions(@RequestParam(required = false) int category, @RequestParam(required = false) String tab){
        if (category == 0 && (tab == null || tab.equals(""))){
            return BaseResponse.fatal("No condition provide");
        }
        Map<String, Object> map = new HashMap<>();
        if (category != 0){ map.put("category", category); }
        if (tab != null && !tab.equals("")){ map.put("tab", tab); }
        return BaseResponse.ok().addResult("blogs", blogMapper.getBlogsByConditions(map));
    }

    @PostMapping("/token/imageUpload")
    public BaseResponse imageUpload(MultipartFile file, HttpServletRequest request) throws IOException {
        // 从token获取sub
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims == null) return WRONG_TOKEN;
        TokenSubject sub = Auth.getSubFromClaim(claims);

        // 文件检测
        if (file == null) return BaseResponse.fatal("No file provided");
        boolean is_img = isImage(file);
        System.out.println("is_img = " + is_img);
        if (!is_img) return BaseResponse.fatal("Only support image file");

        // 获取路径并保存 | 保存后返回文件名并以虚拟路径发送
        String path = FileUtil.newImageFilePath(sub.getId(), request.getServletContext().getRealPath("/"), file.getOriginalFilename());
        System.out.println("path = " + path);
        String name = FileUtil.saveFile(file, path);
        return BaseResponse.ok().addResult("imageName", name);
    }

    @PutMapping("/token/imageUpload")
    public BaseResponse imageUpdate(@RequestParam int blog_id, MultipartFile file, HttpServletRequest request) throws IOException {
        // 从token获取sub
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims == null) return WRONG_TOKEN;
        TokenSubject sub = Auth.getSubFromClaim(claims);

        int user_id = sub.getId();
        Blog blog = blogMapper.getBlogById(blog_id);
        if (blog != null && (userMapper.checkUserAdmin(user_id) != null || blog.getOwnerId() == user_id)){
            // 文件检测
            if (file == null) return BaseResponse.fatal("No file provided");
            boolean is_img = isImage(file);
            System.out.println("is_img = " + is_img);
            if (!is_img) return BaseResponse.fatal("Only support image file");

            // 获取路径并保存 | 保存后返回文件名并以虚拟路径发送
            String path = FileUtil.getImageFilePath(request.getServletContext().getRealPath("/"), blog.getHeadImg());
            System.out.println("path = " + path);
            String name = FileUtil.saveFile(file, path);
            return BaseResponse.ok().addResult("imageName", name);
        } else {
            return BaseResponse.fatal("文章不存在或无权限");
        }
    }

    @PostMapping("/token/htmlUpload")
    public BaseResponse htmlUpload(MultipartFile file, HttpServletRequest request) throws IOException {
        // 从token获取sub
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims == null) return WRONG_TOKEN;
        TokenSubject sub = Auth.getSubFromClaim(claims);

        // 保存文件
        String path = FileUtil.newStringFilePath(sub.getId(), request.getServletContext().getRealPath("/"));
        String name = FileUtil.saveFile(file, path);

        return BaseResponse.ok().addResult("content", name);
    }

    @PutMapping("/token/htmlUpload")
    public BaseResponse htmlUpdate(@RequestParam int blog_id, MultipartFile file, HttpServletRequest request) throws IOException {
        // 从token获取sub
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims == null) return WRONG_TOKEN;
        TokenSubject sub = Auth.getSubFromClaim(claims);

        int user_id = sub.getId();
        Blog blog = blogMapper.getBlogById(blog_id);
        if (blog != null && (userMapper.checkUserAdmin(user_id) != null || blog.getOwnerId() == user_id)){
            // 保存文件
            String path = FileUtil.getStringFilePath(request.getServletContext().getRealPath("/"), blog.getContent());
            String name = FileUtil.saveFile(file, path);
            return BaseResponse.ok().addResult("content", name);
        } else {
            return BaseResponse.fatal("文章不存在或无权限");
        }
    }

    @PostMapping("/token/blogUpload")
    public BaseResponse blogUpload(@RequestBody Blog blog, HttpServletRequest request) throws IOException, ParseException {
        // 从token获取sub
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims ==null){ return WRONG_TOKEN; }
        TokenSubject sub = Auth.getSubFromClaim(claims);
        int id = sub.getId();

        // 设置对象并insert
        if (!isHTMLExist(request.getServletContext().getRealPath("/"), blog.getContent()))
            return BaseResponse.fatal("Content Not Found");
        blog.setOwnerId(sub.getId());
        blog.setUpdateUserId(blog.getOwnerId());
        blog.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        blog.setUpdateTime(blog.getCreateTime());
        System.out.println("blog.toString() = " + blog);
        if (blogMapper.insertOneBlog(blog) > 0){
            int blogId = blog.getId();
            System.out.println("blog.getTabs() = " + blog.getTabs());
            blogMapper.insertTabsByBlogId(blogId, (ArrayList<String>) blog.getTabs());
            if (blogMapper.getUserToBlogCountByIds(blog.getId(), blog.getOwnerId()).size() == 0){
                blogMapper.addUserToBlog(blog.getId(), blog.getOwnerId());
            }
            return BaseResponse.ok().addResult("id", blog.getId());
        } else {
            return BaseResponse.error("Something went wrong.");
        }
    }

    @PutMapping("/token/blogUpload")
    public BaseResponse blogUpdate(@RequestBody Blog blog, HttpServletRequest request) throws IOException {
        // 从token获取sub
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims ==null){ return WRONG_TOKEN; }
        TokenSubject sub = Auth.getSubFromClaim(claims);
        int user_id = sub.getId();

        if (blog != null && (userMapper.checkUserAdmin(user_id) != null || blog.getOwnerId() == user_id)){
            blog.setUpdateUserId(user_id);
            blog.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            if (blogMapper.updateBlogById(blog) > 0){
                blogMapper.truncateTabsById(blog.getId());
                blogMapper.insertTabsByBlogId(blog.getId(), (ArrayList<String>) blog.getTabs());
                return BaseResponse.ok().addResult("id", blog.getId());
            } else return BaseResponse.error("更新失败");
        } else {
            return BaseResponse.fatal("文章不存在或无权限");
        }
    }

    @DeleteMapping("/token/blogUpload")
    public BaseResponse deleteBlog(@RequestParam int blog_id, HttpServletRequest request) throws JsonProcessingException {
        Claims claims = Auth.getClaimFromRequest(request);
        if (claims == null) return WRONG_TOKEN;
        TokenSubject sub = Auth.getSubFromClaim(claims);
        int user_id = sub.getId();
        Blog blog = blogMapper.getBlogById(blog_id);
        return (blog != null && (userMapper.checkUserAdmin(user_id) != null || blog.getOwnerId() == user_id))
                ? ((deleteBlog(request.getServletContext().getRealPath("/"), blog)) ? BaseResponse.ok() : BaseResponse.fatal("删除失败"))
                : BaseResponse.fatal("文章不存在或无权限操作");
    }

    @GetMapping("/categories")
    public BaseResponse getCategories(){
        return BaseResponse.ok().addResult("categories", blogMapper.getCategories());
    }

    @GetMapping("/tabs")
    public BaseResponse getTabs(){
        return BaseResponse.ok().addResult("tabs", blogMapper.getTabs());
    }


    boolean deleteBlog(String base, Blog blog){
        return (blog.getHeadImg() == null || blog.getHeadImg().equals("") || FileUtil.deleteImage(base, blog.getHeadImg()))
                &&
                (FileUtil.deleteText(base, blog.getContent()))
                &&
                (blogMapper.deleteBlogById(blog.getId()) > 0);
    }



}
