package com.financial.business.config;
 
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j整合Swagger3 Api接口文档配置类
 * @author xinyi
 */
@Configuration
public class Knife4jConfig {
 
    /**
     * 创建了一个api接口的分组
     * 除了配置文件方式创建分组，也可以通过注册bean创建分组
     */
    @Bean
    public GroupedOpenApi adminApi() { 
        return GroupedOpenApi.builder()
                // 分组名称
                .group("业务接口")
                // 接口请求路径规则
                .pathsToMatch("/**")
                .build();
    }
 
    /**
     * 配置基本信息
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        // 标题
                        .title("智能理财业务接口文档")
                        // 描述Api接口文档的基本信息
                        .description("后端接口服务...")
                        // 版本
                        .version("v1.0.0")
                        // 设置OpenAPI文档的联系信息，姓名，邮箱。
                        .contact(new Contact().name("xinyi").email("1993262229@qq.com"))
                        // 设置OpenAPI文档的许可证信息，包括许可证名称为"Apache 2.0"，许可证URL为"http://springdoc.org"。
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }
}
 