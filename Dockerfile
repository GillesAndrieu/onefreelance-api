# FROM adoptopenjdk/openjdk15:alpine-jre AS java-build
FROM openjdk:25-jdk AS java-build
ARG module_name
WORKDIR /build/

COPY ${module_name}/build/libs/${module_name}-*.jar application.jar

# extract different artifacts
RUN java -Djarmode=layertools -jar application.jar extract

# ====================================================================================

FROM openjdk:25-jdk
WORKDIR /app/

# Create a layered JAR using spring boot fat jar artifacts
COPY --from=java-build build/dependencies/ ./
COPY --from=java-build build/spring-boot-loader/ ./
COPY --from=java-build build/snapshot-dependencies/ ./
COPY --from=java-build build/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]



