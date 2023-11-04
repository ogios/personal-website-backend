package db

type DBUser struct {
	Username string `json:"username"`
	ID       int    `json:"id"`
	IsAdmin  bool   `json:"is_admin"`
}

// select ================================================
// select ================================================

// 用户名获取用户信息
func GetUserByUsername(username string) (res DBUser, ok bool) {
	SQL := `SELECT id, username, is_admin
            FROM t_user
            WHERE username=?`
	err := DB.Raw(SQL, username).First(&res).Error
	if err == nil {
		ok = true
	}
	return
}

// 登陆用
func GetUserByNamePass(username, password string) (res DBUser, ok bool) {
	SQL := `SELECT id, username, is_admin
            FROM t_user
            WHERE username=? AND password=MD5(?)`
	err := DB.Raw(SQL, username, password).First(&res).Error
	if err == nil {
		ok = true
	}
	return
}

// id获取用户信息
func GetUserByID(id int) (res DBUser, ok bool) {
	SQL := `SELECT id, username, is_admin 
            FROM t_user
            WHERE id=?`
	err := DB.Raw(SQL, id).First(&res).Error
	if err == nil {
		ok = true
	}
	return
}

// 计数
func GetUserCountsByUsername(username string) (res int) {
	SQL := `SELECT count(id)
            FROM t_user
            WHERE username=?`
	DB.Raw(SQL, username).Find(&res)
	return
}

// 计数
func GetUserCountsByID(id int) (res int) {
	SQL := `SELECT count(id)
            FROM t_user
            WHERE id=?`
	DB.Raw(SQL, id).Find(&res)
	return
}

// 计数
func GetUserCounts() (res int) {
	SQL := `SELECT count(id)
            FROM t_user`
	DB.Raw(SQL).Find(&res)
	return
}

// 检查是否为管理员
func CheckIfIsAdmin(id int) (res bool, ok bool) {
	SQL := `SELECT is_admin
            FROM t_user
            WHERE id=?`
	err := DB.Raw(SQL, id).First(&res).Error
	if err == nil {
		ok = true
	}
	return
}

// select ================================================
// select ================================================

func AddUser(username, password string) (res int64) {
	SQL := `INSERT INTO t_user (username, password, is_admin)
            VALUES(?,MD5(?),?)`
	res = DB.Exec(SQL, username, password).RowsAffected
	return
}
