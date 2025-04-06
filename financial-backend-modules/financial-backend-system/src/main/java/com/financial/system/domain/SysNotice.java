package com.financial.system.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.financial.common.core.web.domain.BaseEntity;
import com.financial.common.core.xss.Xss;

/**
 * 通知公告表 sys_notice
 * 
 * @author xinyi
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice")
@Data
@Schema(name = "通知公告表")
public class SysNotice extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    @TableId(type = IdType.AUTO)
    private Long noticeId;

    /** 公告标题 */
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    private String noticeType;

    /** 公告内容 */
    private String noticeContent;

    /** 公告状态（0正常 1关闭） */
    private String status;
}
