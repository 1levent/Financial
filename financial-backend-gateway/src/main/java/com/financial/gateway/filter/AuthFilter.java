package com.financial.gateway.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest.Builder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.financial.common.core.constant.CacheConstants;
import com.financial.common.core.constant.HttpStatus;
import com.financial.common.core.constant.SecurityConstants;
import com.financial.common.core.constant.TokenConstants;
import com.financial.common.core.utils.JwtUtils;
import com.financial.common.core.utils.ServletUtils;
import com.financial.common.core.utils.StringUtils;
import com.financial.common.redis.service.RedisService;
import com.financial.gateway.config.properties.IgnoreWhiteProperties;
import reactor.core.publisher.Mono;

/**
 * 网关鉴权
 * 
 * @author xinyi
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    // 排除过滤的 uri 地址，nacos自行添加
    @Resource
    private IgnoreWhiteProperties ignoreWhite;

    @Resource
    private RedisService redisService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        Builder mutate = request.mutate();

        String url = request.getURI().getPath();
        // 跳过不需要验证的路径
        if (StringUtils.matches(url, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        //token鉴权
        String token = getToken(request);
        if (StringUtils.isEmpty(token)) {
            return unauthorizedResponse(exchange, "令牌不能为空");
        }
        DecodedJWT jwt = JwtUtils.parseToken(token);
        if (jwt == null) {
            return unauthorizedResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userkey = JwtUtils.getUserKey(jwt);
        boolean islogin = redisService.hasKey(getTokenKey(userkey));
        if (!islogin) {
            return unauthorizedResponse(exchange, "登录状态已过期");
        }
        String userid = JwtUtils.getUserId(jwt);
        String username = JwtUtils.getUserName(jwt);

        if (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)) {
            return unauthorizedResponse(exchange, "令牌验证失败");
        }

        // 设置用户信息到请求
        addHeader(mutate, SecurityConstants.USER_KEY, userkey);
        addHeader(mutate, SecurityConstants.DETAILS_USER_ID, userid);
        addHeader(mutate, SecurityConstants.DETAILS_USERNAME, username);
        // 内部请求来源参数清除
        removeHeader(mutate, SecurityConstants.FROM_SOURCE);

        //接口调用耗时统计
        //记录开始时间
        exchange.getAttributes().put("startTime", System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            try{
                //记录接口访问日志
                Long beginVisitTime = exchange.getAttribute("startTime");
                if(beginVisitTime!=null){
                    URI uri = exchange.getRequest().getURI();
                    Map<String,Object> logData = new HashMap<>();
                    logData.put("host",uri.getHost());
                    logData.put("port",uri.getPort());
                    logData.put("path",uri.getPath());
                    logData.put("query",uri.getRawQuery());
                    logData.put("duration",(System.currentTimeMillis()-beginVisitTime)+"ms");

                    log.info("接口访问信息：{}",logData);
                    log.info("----------------------------------\n");
                }
            }catch (Exception e){
                log.error("接口调用耗时统计异常",e);
            }
        }));
//        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private void addHeader(ServerHttpRequest.Builder mutate, String name, Object value) {
        if (value == null) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = ServletUtils.urlEncode(valueStr);
        mutate.header(name, valueEncode);
    }

    private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
        mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String msg) {
        log.error("[鉴权异常处理]请求路径:{},错误信息:{}", exchange.getRequest().getPath(), msg);
        return ServletUtils.webFluxResponseWriter(exchange.getResponse(), msg, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 获取缓存key
     */
    private String getTokenKey(String token) {
        return CacheConstants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 获取请求token
     */
    private String getToken(ServerHttpRequest request) {
        String token = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION_HEADER);
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
            token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
        }
        return token;
    }

    @Override
    public int getOrder() {
        return -200;
    }
}