package com.financial.auth.helper;

import com.financial.auth.service.SysLoginService;
import com.financial.common.core.utils.wx.CodeGenerateUtil;
import com.financial.system.api.model.wx.BaseWxMsgResVo;
import com.financial.system.api.model.wx.WxImgTxtItemVo;
import com.financial.system.api.model.wx.WxImgTxtMsgResVo;
import com.financial.system.api.model.wx.WxTxtMsgResVo;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 微信消息回复
 * @author xinyi
 */
@Slf4j
@Component
public class WxAckHelper {
    @Autowired
    private SysLoginService loginService;
    @Autowired
    private WxLoginHelper qrLoginHelper;
//
//    @Autowired
//    private ChatgptService chatgptService;

    /**
     * 返回自动响应的文本
     *
     * @return
     */
    public BaseWxMsgResVo buildResponseBody(String eventType, String content, String fromUser) {
        // 返回的文本消息
        String textRes = null;
        // 返回的是图文消息
        List<WxImgTxtItemVo> imgTxtList = null;
        if ("subscribe".equalsIgnoreCase(eventType)) {
            // 订阅
            textRes = "感谢你的关注";
        }
//        // 下面是关键词回复
//        else if (chatgptService.inChat(fromUser, content)) {
//            try {
//                textRes = chatgptService.chat(fromUser, content);
//            } catch (Exception e) {
//                log.error("派聪明 访问异常! content: {}", content, e);
//                textRes = "派聪明 出了点小状况，请稍后再试!";
//            }
//        }

        else if ("admin".equalsIgnoreCase(content) || "后台".equals(content) || "002".equals(content)) {
            // admin后台登录，返回对应的用户名 + 密码
            textRes = "后台游客登录账号\n-----------\n登录用户名: guest\n登录密码: 123456";
        }
        // 微信公众号登录
        else if (CodeGenerateUtil.isVerifyCode(content)) {
            loginService.autoRegisterWxUserInfo(fromUser);
            if (qrLoginHelper.login(content)) {
                textRes = "登录成功！";
            } else {
                textRes = "验证码过期了，刷新登录页面重试一下吧";
            }
        } else {
            textRes = "是不是点错了呢";
        }

        if (textRes != null) {
            WxTxtMsgResVo vo = new WxTxtMsgResVo();
            vo.setContent(textRes);
            return vo;
        } else {
            WxImgTxtMsgResVo vo = new WxImgTxtMsgResVo();
            vo.setArticles(imgTxtList);
            vo.setArticleCount(imgTxtList.size());
            return vo;
        }
    }
}
