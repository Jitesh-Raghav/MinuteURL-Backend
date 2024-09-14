FROM openjdk:17
ADD ./Shortner-URL-0.0.1-SNAPSHOT.jar Shortner-URL-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","Shortner-URL-0.0.1-SNAPSHOT.jar"]