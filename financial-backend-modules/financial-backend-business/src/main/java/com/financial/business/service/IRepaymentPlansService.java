package com.financial.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.RepaymentPlans;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.business.entity.dto.RepaymentPlansDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 还款计划表 服务类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
public interface IRepaymentPlansService extends IService<RepaymentPlans> {
  public IPage<RepaymentPlansDTO> list(RepaymentPlansDTO dto, Page<RepaymentPlans> page);
  public void export(RepaymentPlansDTO dto, HttpServletResponse response);
}
