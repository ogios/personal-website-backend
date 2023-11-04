package db

import (
	"pwb/config"

	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

var DB *gorm.DB

func InitDB() {
	d := config.GLOBAL_CONFIG.DBAddr
	db, err := gorm.Open(mysql.Open(d), &gorm.Config{
		PrepareStmt: true,
	})
	if err != nil {
		panic(err)
	}
	DB = db
}
