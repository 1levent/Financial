package com.financial.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.financial.common.security.annotation.EnableCustomConfig;
import com.financial.common.security.annotation.EnableFinancialFeignClients;

/**
 * 定时任务
 * 
 * @author ruoyi
 */
@EnableCustomConfig
@EnableFinancialFeignClients
@SpringBootApplication
public class FinancialJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinancialJobApplication.class, args);
    }
}
