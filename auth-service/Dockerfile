FROM openjdk:17-oracle
WORKDIR /app
COPY ./target/auth-service.jar /app
ENTRYPOINT ["java","-jar","/app/auth-service.jar"]
EXPOSE 8084