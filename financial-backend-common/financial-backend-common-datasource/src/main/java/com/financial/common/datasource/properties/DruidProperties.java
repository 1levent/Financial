package com.financial.common.datasource.properties;

import com.financial.common.datasource.config.DataSourceConfig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
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

    @PostConstruct
    public void init() {
        //打印属性，一个一个打印
        log.info("初始化数据库连接池配置信息：");
        log.info("初始化数据库连接池配置信息：initialSize={}", initialSize);
        log.info("初始化数据库连接池配置信息：minIdle={}", minIdle);
        log.info("初始化数据库连接池配置信息：maxActive={}", maxActive);
        log.info("初始化数据库连接池配置信息：maxWait={}", maxWait);
        log.info("初始化数据库连接池配置信息：timeBetweenEvictionRunsMillis={}", timeBetweenEvictionRunsMillis);
        log.info("初始化数据库连接池配置信息：minEvictableIdleTimeMillis={}", minEvictableIdleTimeMillis);
        log.info("初始化数据库连接池配置信息：validationQuery={}", validationQuery);
        log.info("初始化数据库连接池配置信息：testWhileIdle={}", testWhileIdle);
        log.info("初始化数据库连接池配置信息：testOnBorrow={}", testOnBorrow);
        log.info("初始化数据库连接池配置信息：testOnReturn={}", testOnReturn);
        log.info("初始化数据库连接池配置信息：");
    }
}