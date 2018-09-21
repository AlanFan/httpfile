FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD *.jar /app.jar
ADD start.sh start.sh
ENTRYPOINT ["sh","start.sh"]
