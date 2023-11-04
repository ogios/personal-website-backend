package db

type DBBlog struct {
	Tabs         any    `json:"tabs"`
	UpdateTime   string `json:"updateTime"`
	Title        string `json:"title"`
	HeadImg      string `json:"headImg"`
	Content      string `json:"content"`
	Summary      string `json:"summary"`
	OwnerName    string `json:"ownerName"`
	CreateTime   string `json:"createTime"`
	IsTop        int    `json:"isTop"`
	OwnerID      int    `json:"ownerId"`
	UpdateUserID int    `json:"updateUserId"`
	CategoryID   int    `json:"categoryId"`
	ID           int    `json:"id"`
	IsFinished   int    `json:"isFinished"`
}

// select
// 分页获取所有信息(包括tabs)
func GetBlogByPage(offset, size int) (res []DBBlog, ok bool) {
	SQL := `SELECT b.*,(SELECT username FROM t_user AS u WHERE u.id = b.owner_id) AS owner_name, JSON_ARRAYAGG(t.name) AS tabs
            FROM t_blog b
            LEFT JOIN t_tab t ON b.id = t.blog_id
            GROUP BY b.id
            LIMIT  ?,?`
	err := DB.Raw(SQL, offset, size).Find(&res).Error
	if err == nil {
		ok = true
	}
	return
}

// 获取文章总数
func GetBlogCounts() (res int) {
	SQL := "SELECT count(id) FROM t_blog"
	DB.Raw(SQL).Find(&res)
	return
}

// 根据id获取指定文章
func getBlogById(id int) (res DBBlog, ok bool) {
    SQL := `SELECT b.*, JSON_ARRAYAGG(t.name) AS tabs
            FROM t_blog b
            LEFT JOIN t_tab t ON b.id = t.blog_id
            WHERE id=?
            GROUP BY b.id`
    err := DB.Raw(SQL, id).First(&res).Error
	if err == nil {
		ok = true
	}
	return
}

// 根据blogid和userid获取指定记录 (主要用于更改权限查看)
func getUserToBlogCountByIds(blog_id, user_id int) (res DBBlog, ok bool) {
    SQL := `SELECT * FROM t_user_blog
            WHERE blog_id=? AND user_id=?`
    err := DB.Raw(SQL, blog_id, user_id).First(&res).Error
	if err == nil {
		ok = true
	}
	return
}

func getBlogsByConditions(conditions map[string]any) (res []DBBlog, ok bool) {
    // get params
    tab, tabok := conditions["tab"]
    cate, cateok := conditions["tab"]
	query := `SELECT * FROM t_blog AS b
	        INNER JOIN t_tab AS t ON b.id=t.blog_id `

    // make `where` and args
	query += `WHERE 1=1 `
    args := make([]any, 0)
	if cateok != nil {
        tcate := tab.([]string)
        if len(tcate) > 0 {
            query += `AND b.category_id IN `
            addon := "("
            for i, name := range tcate  {
                addon += "?,"
            }
            addon = addon[:len(addon)-1] + ") "
            args = append(args, tcate...)
        }
	}
	if tabok != nil {
        ttab := tab.([]string)
        if len(ttab) > 0 {
            query += `AND t.name IN `
            addon := "("
            for i, name := range ttab  {
                addon += "?,"
            }
            addon = addon[:len(addon)-1] + ") "
            args = append(args, ttab...)
        }
	}

    // query
    err := DB.Raw(SQL, blog_id, user_id).Find(&res).Error
	if err == nil {
		ok = true
	}
	return
}

// insert
// 插入新的blog| 标题-封面图文件名-文章文件名-摘要-是否完成-创建时间-更新时间-创建者-更新用户-分类
@Insert("INSERT INTO t_blog " +
        "(title, head_img, content, summary, is_finished, is_top, create_time, update_time, owner_id, update_user_id, category_id) " +
        "VALUES(#{title},#{headImg},#{content},#{summary},#{isFinished},#{isTop},#{createTime},#{updateTime},#{ownerId},#{updateUserId},#{categoryId})")
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
