package com.financial.gateway.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import org.springframework.context.annotation.Configuration;

/**
 * sentinel配置
 * @author xinyi
 */
@Configuration
public class SentinelConfig {

  private static final String DATA_ID = "sentinel-financial-backend-gateway";

  @PostConstruct
  void init() {
    Properties properties = new Properties();
    properties.put(PropertyKeyConst.SERVER_ADDR, "127.0.0.1:8848");
    properties.put(PropertyKeyConst.USERNAME,"nacos");
    properties.put(PropertyKeyConst.PASSWORD, "123456");
    ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(
        properties,
        "DEFAULT_GROUP",
        DATA_ID,
        source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
        }));
    FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
  }
}
