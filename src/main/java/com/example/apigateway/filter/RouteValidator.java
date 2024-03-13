package com.example.apigateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> apiList =List.of(

            "/auth/register",
            "/auth/generateToken",
            "/eureka"
    );

    // Must be of reactive package
    public Predicate<ServerHttpRequest> isSecured=
            serverHttpRequest ->
                    apiList.stream().
                            noneMatch(uri -> serverHttpRequest.getURI()
                                    .getPath().contains(uri));
}
