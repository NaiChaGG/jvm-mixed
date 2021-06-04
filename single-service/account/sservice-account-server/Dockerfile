FROM openjdk:11-ea-24-jdk
#FROM hub.c.163.com/library/java:8
ADD target/boundary-passport-server-1.0.0.jar app.jar
VOLUME /tmp
VOLUME /target
RUN bash -c 'touch /app.jar'
EXPOSE 7000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]