package com.example.springtest_backend.mapper;

import com.example.springtest_backend.entity.Blog;
import com.example.springtest_backend.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface BlogMapper {

    // select
    // 分页获取所有信息(包括tabs)
//    @Select("SELECT * FROM t_blog LIMIT #{offset},#{size}")
    @Select("SELECT b.*,(SELECT username FROM t_user AS u WHERE u.id = b.owner_id) AS owner_name, JSON_ARRAYAGG(t.name) AS tabs " +
            "FROM t_blog b " +
            "LEFT JOIN t_tab t ON b.id = t.blog_id " +
            "GROUP BY b.id " +
            "LIMIT  #{offset},#{size}" )
    List<Blog> getBlogByPage(int offset, int size);

    // 获取文章总数
    @Select("SELECT count(id) FROM t_blog")
    int getCounts();

    // 根据id获取指定文章
    @Select("SELECT b.*, JSON_ARRAYAGG(t.name) AS tabs " +
            "FROM t_blog b " +
            "LEFT JOIN t_tab t ON b.id = t.blog_id " +
            "WHERE id=#{id} " +
            "GROUP BY b.id ")
    Blog getBlogById(int id);

    // 根据blogid和userid获取指定记录 (主要用于更改权限查看)
    @Select("SELECT * FROM t_user_blog " +
            "WHERE blog_id=#{blog_id} AND user_id=#{user_id}")
    List<Blog> getUserToBlogCountByIds(int blog_id, int user_id);

    @Select(" <script> " +
            "SELECT * FROM `t_blog` AS b " +
            "<if test=\"#{conditions.tab} != null\"> INNER JOIN `t_tab` AS t ON b.id=t.blog_id </if> " +
            "WHERE 1=1 " +
            "<if test=\"#{conditions.category} != null\"> AND b.category_id=#{conditions.category} </if> " +
            "<if test=\"#{conditions.tab} != null\"> AND t.name=#{conditions.tab} </if> " +
            " </script> ")
    List<Category> getBlogsByConditions(@Param("condition") Map<String, Object> conditions);

    // insert
    // 插入新的blog| 标题-封面图文件名-文章文件名-摘要-是否完成-创建时间-更新时间-创建者-更新用户-分类
    @ResultMap("BaseResultMap")
    @Insert("INSERT INTO t_blog " +
            "(title, head_img, content, summary, is_finished, is_top, create_time, update_time, owner_id, update_user_id, category_id) " +
            "VALUES(#{title},#{headImg},#{content},#{summary},#{isFinished},#{isTop},#{createTime},#{updateTime},#{ownerId},#{updateUserId},#{categoryId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertOneBlog(Blog blog);


    @Insert(" <script> " +
            " INSERT INTO t_tab VALUES " +
            " <foreach collection='tabs' item='tab' separator=','> " +
            " (#{tab},${blog_id}) " +
            " </foreach> " +
            " </script> ")
    int insertTabsByBlogId(int blog_id, @Param("tabs") ArrayList<String> tabs);

    // 为文章加入新的制作者
    @Insert("INSERT INTO t_user_blog VALUES(#{blog_id},#{user_id})")
    int addUserToBlog(int blog_id, int user_id);


    // update
    // 更新文章| 标题-摘要-封面图文件名-更新时间-更新用户-是否完成-是否置顶-分类
    @Update("UPDATE t_blog " +
            "SET title=#{title},summary=#{summary},head_img=#{headImg},update_time=#{updateTime},update_user_id=#{updateUserId},is_finished=#{isFinished},is_top=#{isTop},category_id=#{categoryId} " +
            "WHERE id=#{id}")
    int updateBlogById(Blog blog);

    // delete
    @Delete("DELETE FROM t_blog WHERE id=#{blog_id}")
    int deleteBlogById(int blog_id);


    @Select("SELECT * FROM t_category")
    List<Category> getCategories();

    @Select("SELECT DISTINCT name FROM t_tab")
    List<String> getTabs();

    @Select("SELECT name FROM t_tab WHERE blog_id=#{blog_id}")
    List<String> getTabsById(int blog_id);

    @Delete("DELETE FROM t_tab WHERE blog_id=#{blog_id}")
    int truncateTabsById(int blog_id);

}
