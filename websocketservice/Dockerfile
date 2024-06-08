FROM amazoncorretto:17
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005","-jar","/app.jar"]