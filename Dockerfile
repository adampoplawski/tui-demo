#stage 1 - maven build
FROM maven:3.6.3-amazoncorretto-15 as maven
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn package -DskipTests

#stage 2 - moving file and run
FROM amazoncorretto:15-alpine-jdk
COPY --from=maven target/tui-demo*.jar tui-demo.jar
CMD java ${JAVA_OPTS} -jar tui-demo.jar