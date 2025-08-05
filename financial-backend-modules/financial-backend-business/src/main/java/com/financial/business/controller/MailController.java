package com.financial.business.controller;
import com.financial.business.entity.dto.MailDTO;
import com.financial.business.mail.EmailUtil;
import com.financial.common.security.utils.SecurityUtils;
import com.financial.system.api.domain.SysUser;
import com.financial.system.api.model.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件控制器
 * @author xinyi
 */
@RestController
public class MailController {

  @Resource
  private EmailUtil emailUtil;

  @PostMapping("/business/sendMail")
  @Operation(summary = "发送邮件")
  public void sendMail(@RequestBody MailDTO mailDTO) throws MessagingException {
    LoginUser loginUser = SecurityUtils.getLoginUser();
    SysUser user = loginUser.getSysUser();
    if (user.getEmail() == null) {
      throw new RuntimeException("请先绑定邮箱");
    }
    System.out.println("已向"+user.getEmail()+"发送邮件!");
    emailUtil.sendHtmlMail("1993262229@qq.com", mailDTO.getSubject(), mailDTO.getContent());
  }
}
