package com.financial.auth.form;

import lombok.Data;

/**
 * 微信登录对象
 * @author xinyi
 */
@Data
public class WxLoginVo {
    /**
     * 验证码
     */
    private String code;

    /**
     * 二维码
     */
    private String qr;

    /**
     * true 表示需要重新建立连接
     */
    private boolean reconnect;

}