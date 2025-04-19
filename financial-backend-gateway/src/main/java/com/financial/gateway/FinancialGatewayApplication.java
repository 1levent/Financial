package com.financial.gateway;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 网关启动程序
 * 
 * @author xinyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FinancialGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinancialGatewayApplication.class, args);
        System.out.println("网关启动成功!");
    }

    /**
     * 配置普罗米修斯显示的服务名称
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(){
        return (registry) -> registry.config().commonTags("application", "financial");
    }
}
