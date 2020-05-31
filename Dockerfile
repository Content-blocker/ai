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

RUN mkdir /ai

WORKDIR /ai

ADD ./ai-api/target/ai-api-1.0.0.jar /ai
ADD ./ai-api/python-app /ai/ai-api/python-app

EXPOSE 8082

CMD ["java", "-jar", "ai-api-1.0.0.jar"]