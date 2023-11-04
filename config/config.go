package config

import "flag"

type Config struct {
	DBAddr string
}

var GLOBAL_CONFIG = Config{}

func init() {
	md := flag.String("m", "root:123456@tcp(127.0.0.1:3306)/git_backend?charset=utf8mb4&parseTime=True&loc=Local", "Elasticesearch address and port")
	GLOBAL_CONFIG.DBAddr = *md
}
