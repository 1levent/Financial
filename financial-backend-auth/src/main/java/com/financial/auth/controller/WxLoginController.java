package com.financial.auth.controller;

import com.financial.auth.form.WxLoginVo;
import com.financial.auth.helper.WxLoginHelper;
import com.financial.common.core.domain.R;
import com.financial.common.core.utils.StringUtils;
import jakarta.annotation.Resource;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 公众号登陆的长连接控制器
 *
 * @author xinyi
 **/
@RestController
@Slf4j
public class WxLoginController{
    @Resource
    private WxLoginHelper qrLoginHelper;

    /**
     * 客户端与后端建立扫描二维码的长连接
     *
     * @return
     */
    @ResponseBody
    @GetMapping(path = "subscribe", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter subscribe(String deviceId) throws IOException {
        System.out.println("前端向后端获取连接："+deviceId);
        return qrLoginHelper.subscribe();
    }

    @GetMapping(path = "/login/fetch")
    @ResponseBody
    public String resendCode(String deviceId) throws IOException {
        return qrLoginHelper.resend();
    }

    /**
     * 刷新验证码
     *
     * @return
     * @throws IOException
     */
    @GetMapping(path = "/login/refresh/{deviceId}")
    @ResponseBody
    public R<WxLoginVo> refresh(@PathVariable("deviceId") String deviceId) throws IOException {
        WxLoginVo vo = new WxLoginVo();
        String code = qrLoginHelper.refreshCode();
        System.out.println("新验证码："+code);
        if (StringUtils.isBlank(code)) {
            // 刷新失败，之前的连接已失效，重新建立连接
            vo.setCode(code);
            vo.setReconnect(true);
        } else {
            vo.setCode(code);
            vo.setReconnect(false);
        }
        return R.ok(vo);
    }
}
