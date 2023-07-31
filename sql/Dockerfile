FROM mysql:latest

MAINTAINER ogios:2134692955@qq.com


# root密码 记得修改
ENV MYSQL_ROOT_PASSWORD=123456

# 初始化自动创建库和表
ADD git_backend.sql /docker-entrypoint-initdb.d/

# 持久化数据
VOLUME /var/lib/mysql