package com.example.apigateway.filter;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidate {
    public static final List<String> openApiEndpoints = List.of("/auth/singin", "/eureka");

    public Predicate<ServerHttpRequest> isSecured = serverHttpRequest -> openApiEndpoints.parallelStream().noneMatch(url -> serverHttpRequest.getURI().getPath().contains(url));
}
