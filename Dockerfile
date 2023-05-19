FROM ubuntu:focal AS ubuntu-selenium
MAINTAINER gaofubo

RUN cp /etc/apt/sources.list /etc/apt/sources.list.bak

RUN echo '\
deb http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse \
deb http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse \
deb http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse \
deb http://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse \
deb http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse \
deb-src http://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse \
deb-src http://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse \
deb-src http://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse \
deb-src http://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse \
deb-src http://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse' > /etc/apt/sources.list

RUN apt update

RUN apt install -y chromium-browser

RUN apt install -y chromium-chromedriver
# /usr/bin/chromedriver
RUN apt install -y openjdk-8-jdk-headless

FROM ubuntu-selenium
MAINTAINER gaofubo

ENV PARAMS=""
ENV JAVA_OPTS=""

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD target/cloud-ticket-*.jar /project/app.jar
# SSL证书
#ADD /Users/gaofubo/data/common/jdk8_security/* /usr/local/openjdk-8/lib/security

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /project/app.jar $PARAMS"]

# docker build
# docker build -t gaofubo/cloud-ticket:v1.0 .

# docker tag gaofubo/cloud-ticket:v1.0 registry.cn-hangzhou.aliyuncs.com/gaofubo/cloud-ticket:v1.0

# docker push registry.cn-hangzhou.aliyuncs.com/gaofubo/cloud-ticket:v1.0
# docker pull registry.cn-hangzhou.aliyuncs.com/gaofubo/cloud-ticket:v1.0
