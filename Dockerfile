FROM ubuntu:20.04

# Setup python and java and base system
ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y apt-utils

RUN apt-get install -y \
        python3 \
        python3-pip \
        python3-dev

RUN apt-get install -y openjdk-8-jdk

RUN pip3 install --upgrade setuptools
RUN pip3 install wheel
RUN pip3 install scipy
RUN pip3 install --upgrade gensim

RUN apt-get install -y curl unzip

RUN mkdir /ai && mkdir /ai/ai-api
WORKDIR /ai

RUN mkdir /ai/ai-api/models
RUN curl -fsSL -o /ai/ai-api/models/enwiki_dbow.zip "https://storage.googleapis.com/contentblocker/enwiki_dbow.zip" && \
    unzip /ai/ai-api/models/enwiki_dbow.zip -d /ai/ai-api/models/

EXPOSE 8082

ADD ./ai-api/target/ai-api-1.0.0.jar /ai
ADD ./ai-api/python-app /ai/ai-api/python-app

CMD ["java", "-jar", "ai-api-1.0.0.jar"]