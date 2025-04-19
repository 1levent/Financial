package com.financial.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.BudgetManagement;
import com.financial.business.entity.BudgetTracking;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.business.entity.dto.BudgetTrackingDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 预算跟踪表 服务类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
public interface IBudgetTrackingService extends IService<BudgetTracking> {
  public IPage<BudgetTrackingDTO> list(BudgetTrackingDTO dto, Page<BudgetTracking> page);

  public void getInfoByBudgetId(Long budgetId,BudgetTrackingDTO dto);
}
