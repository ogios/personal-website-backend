FROM joengenduvel/jre17:latest

MAINTAINER ogios:2134692955@qq.com


WORKDIR /opt/server

EXPOSE 8080

ADD ./target/git_backend*.jar /opt/server/

RUN mkdir /opt/server/public/

CMD java -jar ./*.jar

