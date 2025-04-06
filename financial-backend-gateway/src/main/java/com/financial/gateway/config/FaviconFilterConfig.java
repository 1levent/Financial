package com.financial.gateway.config;

import com.financial.common.core.constant.HttpStatus;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * favicon 过滤器配置
 * @author xinyi
 */
@Configuration
public class FaviconFilterConfig {
    @Bean
    public FilterRegistrationBean<Filter> faviconFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter((request, response, chain) -> {
            if ("/favicon.ico".equals(((HttpServletRequest) request).getRequestURI())) {
                ((HttpServletResponse) response).setStatus(HttpStatus.NO_CONTENT);
                return;
            }
            chain.doFilter(request, response);
        });
        registration.addUrlPatterns("/favicon.ico");
        return registration;
    }
}