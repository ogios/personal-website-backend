package main

import (
	"pwb/db"
	"pwb/router"

	"github.com/gin-gonic/gin"
)

func main() {
	db.InitDB()
	r := gin.Default()
	router.InitRouter(r)
	if err := r.Run(); err != nil {
		panic(err)
	}
}
