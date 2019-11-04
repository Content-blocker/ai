FROM openjdk:8

RUN mkdir /ai

WORKDIR /ai

ADD ./ai-api/target/ai-api-1.0.0.jar /ai

EXPOSE 8080

CMD ["java", "-jar", "ai-api-1.0.0.jar"]