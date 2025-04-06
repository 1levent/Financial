package com.financial.gateway.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import com.financial.gateway.handler.ValidateCodeHandler;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 路由配置信息
 * 
 * @author xinyi
 */
@Configuration
public class RouterFunctionConfiguration {
    @Resource
    private ValidateCodeHandler validateCodeHandler;

    @SuppressWarnings("rawtypes")
    @Bean
    public RouterFunction routerFunction() {
        return RouterFunctions.route(
                RequestPredicates.GET("/code").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                validateCodeHandler);
    }

    @Bean
    public RouterFunction<ServerResponse> staticResourceRouter() {
        return RouterFunctions.resources("/doc.html", new ClassPathResource("META-INF/resources/doc.html"))
            .and(RouterFunctions.resources("/webjars/**", new ClassPathResource("META-INF/resources/webjars/")));
    }
}
