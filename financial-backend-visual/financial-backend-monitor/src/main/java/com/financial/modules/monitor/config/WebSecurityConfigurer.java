package com.financial.modules.monitor.config;

import static org.springframework.security.config.Customizer.withDefaults;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * 监控权限配置
 * 
 * @author xinyi
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer {
    private final String adminContextPath;

    public WebSecurityConfigurer(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requestMatcherRegistry) -> requestMatcherRegistry.anyRequest().authenticated())
            .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * 客户端注册直接放过 解决服务端增加登录后客户端无法注册一直报401问题
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(adminContextPath + "/instances", adminContextPath + "/actuator/**");
    }
}

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
//        successHandler.setTargetUrlParameter("redirectTo");
//        successHandler.setDefaultTargetUrl(adminContextPath + "/");
//
//        return httpSecurity
//            .headers(headers -> headers
//                .frameOptions(FrameOptionsConfig::disable) // 允许iframe嵌入
//            )
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers(
//                    ant(adminContextPath + "/assets/**"),
//                    ant(adminContextPath + "/login"),
//                    ant(adminContextPath + "/actuator/**"),
//                    ant(adminContextPath + "/instances/**")
//                    //antMatchers("/favicon.ico", "/doc.html", "/webjars/**")
////                    ant(adminContextPath + "/favicon.ico"),
////                    ant(adminContextPath + "/doc.html"),
////                    ant(adminContextPath + "/webjars/**")
//                ).permitAll()
//                .anyRequest()
//                .authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage(adminContextPath + "/login")
//                .successHandler(successHandler)
//            )
//            .logout(logout -> logout
//                .logoutUrl(adminContextPath + "/logout")
//            )
//            .httpBasic(Customizer.withDefaults())
//            .csrf(AbstractHttpConfigurer::disable)
//            .build();
//    }
//
//    // 辅助方法简化ant路径匹配
//    private static String ant(String pattern) {
//        return "antMatchers=" + pattern;
//    }
//}
