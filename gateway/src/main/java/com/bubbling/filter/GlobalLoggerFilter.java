package com.bubbling.filter;

import com.auth0.jwt.exceptions.*;
import com.bubbling.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class GlobalLoggerFilter implements GlobalFilter, Ordered {
    @Autowired
    public ErrorWebExceptionHandler errorWebExceptionHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Mono<Void> result;
        Date begin=new Date();
        log.info("[into] request time: "+begin.getTime());

        Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
        String method= uriVariables.get("api");
        String token= uriVariables.get("token");
        log.info("[api] request api: "+method);
        log.info("[token] carry jwt: "+token);
        if("login".equals(method))
            result=chain.filter(exchange);
        else {
            try {
                JWTUtil.verify(token).getClaim("userPhone");
                result=chain.filter(exchange);
            }catch (TokenExpiredException e){
                exchange.getResponse().setStatusCode(HttpStatus.GONE);
                return exchange.getResponse().setComplete();
            }catch (JWTVerificationException | JWTCreationException e){
                exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                return exchange.getResponse().setComplete();
            }catch (Exception e){
                log.error(e.getMessage(), e);
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                return exchange.getResponse().setComplete();
            }finally {
                Date end=new Date();
                log.info("[out] response time: "+end.getTime());
            }
        }
        Date end=new Date();
        log.info("[out] response time: "+end.getTime());
        return result;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
