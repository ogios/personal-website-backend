FROM joengenduvel/jre17:latest

MAINTAINER ogios:2134692955@qq.com

RUN ["mkdir", "-p", "/opt/server/public/"]

#ADD ./target/git_backend*.jar /opt/server/

WORKDIR /opt/server/

EXPOSE 8080

CMD java -jar /opt/server/*.jar

