package com.example.apigateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    public GlobalFilter(){
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        // Custome PreFilter

        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global Filter base message: request id -> {}",config.getBaseMessage());

            if( config.isPreLogger()){
                log.info("Global Filter Start : request id -> {}",request.getId());
            }
            return  chain.filter(exchange).then(Mono.fromRunnable(()->{ // MONO는 webflux에서 지원하는 방식으로 비동기방식으로 단일 값을 전달할 때 사용된다.
                if( config.isPostLogger()){
                    log.info("Global Filter End : Response  -> {} ",response.getStatusCode());
                }
            }));

        });
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

    }
}
