package com.financial.auth;

import com.financial.common.security.annotation.EnableCustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.financial.common.security.annotation.EnableFinancialFeignClients;

/**
 * 认证授权中心
 * 
 * @author xinyi
 */
@EnableFinancialFeignClients(basePackages = {"com.financial.system.api"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FinancialAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinancialAuthApplication.class, args);
        System.out.println("认证授权中心启动成功!");
    }
}
