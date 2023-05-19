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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class GlobalLoggerFilter implements GlobalFilter, Ordered {
    @Autowired
    public ErrorWebExceptionHandler errorWebExceptionHandler;

    /**
     * 网关gateway过滤器, 拦截不合法的jwt-token, 并记录访问信息
     * 2022-03-20 09:51:46 GMT+8
     * @author k
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Mono<Void> result;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("[into] request time: "+formatter.format(new Date().getTime()));

        Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
        String method= uriVariables.get("api");
        String token= uriVariables.get("token");
        log.info("[api] request api: "+method);

        if("login".equals(method)){
            log.info("[userPhone] in carry jwt: "+token);
            log.info("[out] response time: "+formatter.format(new Date().getTime()));
            result=chain.filter(exchange);
        }else if("register".equals(method)){
            log.info("[userPhone] in carry jwt: "+token);
            log.info("[out] response time: "+formatter.format(new Date().getTime()));
            result=chain.filter(exchange);
        }else {
            try {
                String userPhone = JWTUtil.verify(token).getClaim("userPhone").toString().replace("\"", "");
                log.info("[userPhone] in carry jwt: "+userPhone);
                result=chain.filter(exchange);
            }//捕获到jwt异常，则解析jwt失败，获取不到userPhone，则日志输出token
            catch (TokenExpiredException e){
                log.error(e.getMessage(), e);
                log.info("[token] in carry jwt: "+token);
                exchange.getResponse().setStatusCode(HttpStatus.GONE);
                return exchange.getResponse().setComplete();
            }catch (JWTVerificationException | JWTCreationException e){
                log.error(e.getMessage(), e);
                log.info("[token] in carry jwt: "+token);
                exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                return exchange.getResponse().setComplete();
            }catch (Exception e){
                log.error(e.getMessage(), e);
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                return exchange.getResponse().setComplete();
            }finally {
                Date end=new Date();
                log.info("[out] response time: "+formatter.format(new Date().getTime()));
            }
        }
        return result;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
