package com.financial.common.core.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * xxl-job定时任务配置类
 * @author xinyi
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobConfig {
  @Value("${admin.addresses}")
  private String adminAddresses;

  @Value("${accessToken}")
  private String accessToken;

  @Value("${executor.appname}")
  private String appname;

  @Value("${executor.address}")
  private String address;

  @Value("${executor.ip}")
  private String ip;

  @Value("${executor.port}")
  private int port;

  @Value("${executor.logpath}")
  private String logPath;

  @Value("${executor.logretentiondays}")
  private int logRetentionDays;

  @Bean
  public XxlJobSpringExecutor xxlJobExecutor() {
    log.info(">>>>>>>>>>> xxl-job config init.");
    XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
    xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
    xxlJobSpringExecutor.setAppname(appname);
    xxlJobSpringExecutor.setAddress(address);
    xxlJobSpringExecutor.setIp(ip);
    xxlJobSpringExecutor.setPort(port);
    xxlJobSpringExecutor.setAccessToken(accessToken);
    xxlJobSpringExecutor.setLogPath(logPath);
    xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
    return xxlJobSpringExecutor;
  }

}
