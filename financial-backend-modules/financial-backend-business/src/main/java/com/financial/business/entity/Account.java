package com.financial.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.financial.common.core.web.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import lombok.Data;

/**
 * <p>
 * 账户表
 * </p>
 *
 * @author xinyi
 * @since 2025-04-09
 */
@Data
@TableName("account")
@Schema(description = "账户表")
public class Account extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "账户号")
    private String accountNo;

    @Schema(description = "账户类型")
    private String accountType;

    @Schema(description = "所属机构")
    private String institution;

    @Schema(description = "资产")
    private BigDecimal amount;

    @Schema(description = "逻辑删除")
    private boolean deleted;

}
