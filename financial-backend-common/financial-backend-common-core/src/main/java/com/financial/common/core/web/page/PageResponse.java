package com.financial.common.core.web.page;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * 统一分页响应对象
 * @author xinyi
 * @param <T> 数据类型
 */
@Getter
@Setter
public class PageResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

  //------------------------ Getter/Setter ------------------------
  // 省略常规方法...
  /** 状态信息 */
    private int code = 200;
    private String msg = "success";

    /** 分页元数据 */
    private int pageNum;
    private int pageSize;
    private long total;
    private int totalPages;

    /** 业务数据 */
    private List<T> data;

    //------------------------ 核心构造函数 ------------------------
    public PageResponse(int pageNum, int pageSize, List<T> data, long total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.data = data;
        this.total = total;
        this.totalPages = calculateTotalPages();
    }

    public PageResponse() {

    }

    //------------------------ 静态工厂方法 ------------------------
    /** 成功响应 */
    public static <T> PageResponse<T> success(int pageNum, int pageSize, List<T> data, long total) {
        return new PageResponse<>(pageNum, pageSize, data, total);
    }

    /** 空数据响应 */
    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(1, 10, Collections.emptyList(), 0);
    }

    /** 错误响应 */
    public static <T> PageResponse<T> error(int code, String msg) {
        PageResponse<T> response = new PageResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    //------------------------ 工具方法 ------------------------
    /** 计算总页数 */
    private int calculateTotalPages() {
      if (pageSize == 0) {
        return 0;
      }
        return (int) Math.ceil((double) total / pageSize);
    }
}