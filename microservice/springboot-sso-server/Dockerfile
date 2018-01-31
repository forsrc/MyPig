#FROM openjdk:8-jdk-alpine
FROM forsrc/centos-jdk-ssh
VOLUME /tmp
ARG ARG_JAR_FILE
ARG ARG_RES_FILE
ARG ARG_PORT

RUN mkdir app

ADD ${ARG_JAR_FILE} app/app.jar

RUN mkdir app/config
ADD ${ARG_RES_FILE} app/config

ADD entrypoint.sh /
RUN chmod u+x /entrypoint.sh

RUN chmod -R u+x /app

EXPOSE ${ARG_PORT}

ENTRYPOINT /entrypoint.sh
