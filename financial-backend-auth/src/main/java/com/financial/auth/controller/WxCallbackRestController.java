package com.financial.auth.controller;

import com.financial.auth.helper.WxAckHelper;
import com.financial.auth.helper.WxLoginHelper;
import com.financial.auth.service.SysLoginService;
import com.financial.common.core.utils.StringUtils;
import com.financial.system.api.model.wx.BaseWxMsgResVo;
import com.financial.system.api.model.wx.WxTxtMsgReqVo;
import com.financial.system.api.model.wx.WxTxtMsgResVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信公众号登录相关
 *
 * @author xinyi
 */
@RequestMapping(path = "wx")
@RestController
public class WxCallbackRestController {
    @Resource
    private SysLoginService loginService;
    @Resource
    private WxLoginHelper qrLoginHelper;
    @Resource
    private WxAckHelper wxHelper;

    /**
     * 微信的公众号接入 token 验证，即返回echostr的参数值
     * @param request 请求
     * @return 参数
     */
    @GetMapping(path = "callback")
    public String check(HttpServletRequest request) {
        String echoStr = request.getParameter("echostr");
        if (StringUtils.isNoneEmpty(echoStr)) {
            return echoStr;
        }
        return "";
    }

    /**
     * fixme: 需要做防刷校验
     * 微信的响应返回
     * 本地测试访问: curl -X POST 'http://localhost:8080/wx/callback' -H 'content-type:application/xml' -d '<xml><URL><![CDATA[https://hhui.top]]></URL><ToUserName><![CDATA[一灰灰blog]]></ToUserName><FromUserName><![CDATA[demoUser1234]]></FromUserName><CreateTime>1655700579</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[login]]></Content><MsgId>11111111</MsgId></xml>' -i
     *
     * @param msg
     * @return
     */
    @PostMapping(path = "callback",
            consumes = {"application/xml", "text/xml"},
            produces = "application/xml;charset=utf-8")
    public BaseWxMsgResVo callBack(@RequestBody WxTxtMsgReqVo msg) {
        String content = msg.getContent();
        System.out.println("微信接收到的消息："+content);
        if ("subscribe".equals(msg.getEvent()) || "scan".equalsIgnoreCase(msg.getEvent())) {
            String key = msg.getEventKey();
            if (StringUtils.isNotBlank(key) || key.startsWith("qrscene_")) {
                // 带参数的二维码，扫描、关注事件拿到之后，直接登录，省却输入验证码这一步
                // fixme 带参数二维码需要 微信认证，个人公众号无权限
                String code = key.substring("qrscene_".length());
                loginService.autoRegisterWxUserInfo(msg.getFromUserName());
                qrLoginHelper.login(code);
                WxTxtMsgResVo res = new WxTxtMsgResVo();
                res.setContent("登录成功");
                fillResVo(res, msg);
                return res;
            }
        }

        BaseWxMsgResVo res = wxHelper.buildResponseBody(msg.getEvent(), content, msg.getFromUserName());
        fillResVo(res, msg);
        return res;
    }

    private void fillResVo(BaseWxMsgResVo res, WxTxtMsgReqVo msg) {
        res.setFromUserName(msg.getToUserName());
        res.setToUserName(msg.getFromUserName());
        res.setCreateTime(System.currentTimeMillis() / 1000);
    }
}