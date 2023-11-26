package com.example.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter(){
        super(Config.class);
    }
    public static class Config {
            // put the configuration properties

    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custome PreFilter

        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE filter: request id -> {}",request.getId());
            // Custom Post Filter
            return  chain.filter(exchange).then(Mono.fromRunnable(()->{ // MONO는 webflux에서 지원하는 방식으로 비동기방식으로 단일 값을 전달할 때 사용된다.
                log.info("Custom POST filter: response code -> {}",response.getStatusCode());
            }));

        });
    }
}
