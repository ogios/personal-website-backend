package router

import "github.com/gin-gonic/gin"

func InitRouter(engin *gin.Engine) {
	addUser(engin)
	addBlog(engin)
	addDefault(engin)
}

func addDefault(engin *gin.Engine) {
	engin.NoRoute(func(ctx *gin.Context) {
		ctx.String(401, "This request will be recorded")
	})
}
