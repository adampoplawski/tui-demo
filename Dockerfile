#stage 1 - maven build
FROM 731576701570.dkr.ecr.us-east-1.amazonaws.com/maven as maven
COPY ./pom.xml ./pom.xml
RUN mvn dependency:go-offline -B
COPY ./src ./src
RUN mvn package -DskipTests

#stage 2 - moving file and run
FROM 731576701570.dkr.ecr.us-east-1.amazonaws.com/corretto
COPY --from=maven target/tui-demo*.jar tui-demo.jar
CMD java ${JAVA_OPTS} -jar tui-demo.jar