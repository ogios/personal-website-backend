#!/usr/bin/sh

docker run -d -p 3306:3306 --name git-backend-mysql \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-v /mydata/mysql/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD= \
-d git-backend-mysql

