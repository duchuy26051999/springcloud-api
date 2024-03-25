FROM openjdk:17-oracle
WORKDIR /app
COPY ./target/eureka.jar /app
ENTRYPOINT ["java","-jar","/app/eureka.jar"]
EXPOSE 8080