package com.financial.common.datasource.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * druid 配置属性
 * @author xinyi
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource.dynamic.druid")
@Getter
@Setter
public class DruidProperties {
    private static final Logger log = LoggerFactory.getLogger(DruidProperties.class);

    private int initialSize;
    private int minIdle;
    private int maxActive;
    private int maxWait;
    private int timeBetweenEvictionRunsMillis;
    private int minEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;

}