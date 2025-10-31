FROM bellsoft/liberica-openjre-debian:24-cds
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts settings.gradle ./
COPY discovery-server ./discovery-server

RUN ./gradlew :discovery-server:bootJar --no-daemon

# Perform the extraction in a separate builder container
FROM bellsoft/liberica-openjre-debian:24-cds AS builder
WORKDIR /builder

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar

RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# This is the runtime container
FROM bellsoft/liberica-openjre-debian:24-cds
WORKDIR /application

COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

ENTRYPOINT ["java", "-jar", "application.jar"]
