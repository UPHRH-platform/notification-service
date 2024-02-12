FROM openjdk:11
MAINTAINER haridas <haridas.kakunje@tarento.com>
ADD target/notification-0.0.1-SNAPSHOT.jar notification-0.0.1-SNAPSHOT.jar
COPY src/main/resources/upsmf.json /opt/firebase.json
#ADD public/emails emails
ENTRYPOINT ["java", "-jar", "/notification-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
