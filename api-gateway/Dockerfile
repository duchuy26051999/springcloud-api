FROM openjdk:17-oracle
WORKDIR /app
COPY ./target/api-gateway.jar /app
ENTRYPOINT ["java","-jar","/app/api-gateway.jar"]
EXPOSE 8080