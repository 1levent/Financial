package com.financial.common.datasource.config;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceBuilder;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 数据源配置
 * @author xinyi
 */
@Configuration
public class DataSourceConfig {

  private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

  @Bean("master")
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.dynamic.datasource.master")
  public DataSource dataSource() {
    return DruidDataSourceBuilder.create().build();
//    MyRoutingDataSource routingDataSource = new MyRoutingDataSource();
//    // 显式设置数据源映射
//    Map<Object, Object> targetDataSources = new HashMap<>();
//    targetDataSources.put("master", masterDataSource);
//    routingDataSource.setTargetDataSources(targetDataSources);
//    routingDataSource.setDefaultTargetDataSource(masterDataSource);
//    routingDataSource.afterPropertiesSet(); // 触发初始化
//    return routingDataSource;
  }

}