package com.example.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory {


    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    public RestTemplate restTemplate;
    public AuthenticationFilter() {
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Object config) {
        return (((exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())){

                //header contains token or not
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                {
                    throw new RuntimeException("Token is missing");
                }

                String authHeader= exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader!=null && authHeader.startsWith("Bearer ")) {
                    authHeader= authHeader.substring(7);

                }
                try {
                    //Rest call to auth service
                    restTemplate.getForObject("http://AUTHENTICATION-SERVICE/auth/validate?token"+authHeader,String.class);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return chain.filter(exchange);

        }));
    }
    public static class Config {

    }
}
