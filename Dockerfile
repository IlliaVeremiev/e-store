FROM amazoncorretto:11
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY /target/estore-0.0.1-SNAPSHOT.jar app.jar
CMD ["java","-jar","app.jar"]