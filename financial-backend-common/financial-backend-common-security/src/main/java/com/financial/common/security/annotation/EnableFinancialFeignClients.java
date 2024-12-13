package com.financial.common.security.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;
import java.lang.annotation.*;

/**
 * 自定义feign注解
 * 添加basePackages路径
 * 
 * @author levent
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
public @interface EnableFinancialFeignClients
{
    String[] value() default {};

    String[] basePackages() default { "com.financial" };

    Class<?>[] basePackageClasses() default {};

    Class<?>[] defaultConfiguration() default {};

    Class<?>[] clients() default {};
}
