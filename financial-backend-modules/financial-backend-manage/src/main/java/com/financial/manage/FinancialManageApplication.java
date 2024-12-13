package com.financial.manage;

import com.financial.common.security.annotation.EnableCustomConfig;
import com.financial.common.security.annotation.EnableFinancialFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: guanxinyi
 * @CreateTime: 2024-11-18
 * @Description: 管理启动类
 * @Version: 1.0
 */
@EnableCustomConfig
@EnableFinancialFeignClients
@SpringBootApplication
public class FinancialManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinancialManageApplication.class, args);
    }
}
