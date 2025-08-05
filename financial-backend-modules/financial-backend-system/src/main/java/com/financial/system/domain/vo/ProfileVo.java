package com.financial.system.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "个人信息")
public class ProfileVo {
  @Schema(description = "用户ID")
  private Long userId;
  @Schema(description = "账号")
  private String userName;
  @Schema(description = "昵称")
  private String nickName;
  @Schema(description = "头像")
  private String avatar;
  @Schema(description = "性别")
  private String sex;
  @Schema(description = "邮箱")
  private String email;
  @Schema(description = "手机号码")
  private String phonenumber;
}
