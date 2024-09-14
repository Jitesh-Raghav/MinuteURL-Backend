FROM openjdk:17
ADD ./ProAssign-0.0.1-SNAPSHOT.jar ProAssign-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","ProAssign-0.0.1-SNAPSHOT.jar"]