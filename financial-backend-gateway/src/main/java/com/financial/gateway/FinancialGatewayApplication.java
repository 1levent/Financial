package com.financial.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关启动程序
 * 
 * @author xinyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class FinancialGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinancialGatewayApplication.class, args);
        System.out.println("网关启动成功!");
    }
}
