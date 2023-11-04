package router

import "github.com/gin-gonic/gin"

func addUser(r *gin.Engine) {
	api := r.Group("/api/sign")
	api.POST("login")
}
