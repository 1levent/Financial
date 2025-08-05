package com.financial.business.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.financial.business.entity.param.XxlJobInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * xxl-job工具类
 * @author xinyi
 */
@Slf4j
@Component
public class XxlJobUtil {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobUtil.class);
 
    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;
 
    @Value("${xxl.job.executor.appname}")
    private String appname;

    @Value("${xxl.job.accessToken}")
    private String accessToken;

    private RestTemplate restTemplate = new RestTemplate();
 
    private static final String ADD_URL = "/jobinfo/addJob";
    private static final String UPDATE_URL = "/jobinfo/updateJob";
    private static final String REMOVE_URL = "/jobinfo/removeJob";
    private static final String PAUSE_URL = "/jobinfo/pauseJob";
    private static final String START_URL = "/jobinfo/startJob";
    private static final String ADD_START_URL = "/jobinfo/addAndStart";
    private static final String GET_GROUP_ID = "/jobgroup/pageList";


    // 新增成员变量存储Cookie
    private Map<String, String> loginCookie = new HashMap<>();

    public void loginXxlJob() {
        logger.info("开始登录xxl-job");
        String loginUrl = adminAddresses + "/login";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userName", "admin");
        params.add("password", "123456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info("请求头："+headers);
        RequestEntity<MultiValueMap<String, String>> requestEntity = new RequestEntity<>(
            params, headers, HttpMethod.POST, URI.create(loginUrl)
        );
        log.info("请求参数："+requestEntity);

        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        // 提取Cookie并存储到Map
        List<String> cookies = response.getHeaders().get("Set-Cookie");
        Optional<String> cookieOpt = cookies.stream()
            .filter(c -> c.startsWith("XXL_JOB_LOGIN_IDENTITY="))
            .findFirst();

        if (cookieOpt.isPresent()) {
            loginCookie.put("Cookie", cookieOpt.get().split(";")[0]);
        } else {
            log.error("登录失败，响应头未包含有效Cookie[6,10](@ref)");
            throw new RuntimeException("登录失败，响应头未包含有效Cookie[6,10](@ref)");
        }
    }
 
    public String add(XxlJobInfo jobInfo) {
//        // 查询对应groupId:
//        JSONObject param = new JSONObject();
//        param.put("appname", appname);
//        param.put("current", 1);   // 必须为整型
//        param.put("size", 10);     // 必须为整型
//        // 正确获取groupId
//        String result = doPost(adminAddresses + GET_GROUP_ID,param.toJSONString());
//        JSONObject jsonObject = JSON.parseObject(result);
//        String groupId = jsonObject.getJSONObject("data")
//            .getJSONArray("records")
//            .getJSONObject(0)
//            .getString("id");
        jobInfo.setJobGroup(2);
        String json = JSON.toJSONString(jobInfo);
        return doPost(adminAddresses + ADD_URL, json);
    }
 
    public String update(int id, String cron){
        Map<String,Object> param = new HashMap<>();
        param.put("id", id);
//        param.put("jobCron", cron);
        param.put("scheduleConf", cron);
        String json = JSON.toJSONString(param);
        return doPost(adminAddresses + UPDATE_URL, json);
    }
 
    public String remove(int id){
        Map<String,Object> param = new HashMap<>();
        param.put("id", id);
        String json = JSON.toJSONString(param);
        return doPost(adminAddresses + REMOVE_URL, json);
    }
 
    public String pause(int id){
        Map<String,Object> param = new HashMap<>();
        param.put("id", id);
        String json = JSON.toJSONString(param);
        return doPost(adminAddresses + PAUSE_URL, json);
    }
 
    public String start(int id){
        Map<String,Object> param = new HashMap<>();
        param.put("id", id);
        String json = JSON.toJSONString(param);
        return doPost(adminAddresses + START_URL, json);
    }
 
    public String addAndStart(XxlJobInfo jobInfo){
        Map<String,Object> param = new HashMap<>();
        param.put("appname", appname);
        String json = JSON.toJSONString(param);
        String result = doPost(adminAddresses + GET_GROUP_ID, json);
 
        JSONObject jsonObject = JSON.parseObject(result);
        String groupId = jsonObject.getString("content");
        jobInfo.setJobGroup(Integer.parseInt(groupId));
        String json2 = JSON.toJSONString(jobInfo);
 
        return doPost(adminAddresses + ADD_START_URL, json2);
    }
 
    public String doPost(String url, String json){
        // 检查登录状态
        if (loginCookie.isEmpty()) {
            loginXxlJob(); // 自动触发登录
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAll(loginCookie);
        headers.add("XXL-JOB-ACCESS-TOKEN", accessToken);
        HttpEntity<String> entity = new HttpEntity<>(json ,headers);
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, entity, String.class);
        // 处理401重新登录
        if (stringResponseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            loginXxlJob();
            return doPost(url, json);
        }
        JSONObject body = JSON.parseObject(stringResponseEntity.getBody());
        return body.getString("content");
    }
 
}