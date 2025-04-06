package com.financial.business;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.financial.common.security.annotation.EnableCustomConfig;
import com.financial.common.security.annotation.EnableFinancialFeignClients;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 业务类
 * @author xinyi
 */
@EnableCustomConfig
@EnableFinancialFeignClients(basePackages = {"com.financial.system.api"})
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    MybatisPlusAutoConfiguration.class,
    DruidDataSourceAutoConfigure.class,
    DynamicDataSourceAutoConfiguration.class
})
@SpringBootConfiguration
@MapperScan(basePackages = "com.financial.business.mapper")
public class FinancialBusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinancialBusinessApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  业务模块启动成功   ლ(´ڡ`ლ)ﾞ");
    }
}
