FROM openjdk:17-oracle
WORKDIR /app
COPY ./target/customer-service.jar /app
ENTRYPOINT ["java","-jar","/app/customer-service.jar"]
EXPOSE 8080