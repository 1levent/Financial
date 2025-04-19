package com.financial.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.BudgetManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.business.entity.dto.BudgetManagementDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 预算管理表 服务类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
public interface IBudgetManagementService extends IService<BudgetManagement> {

  public IPage<BudgetManagementDTO> list(BudgetManagementDTO dto, Page<BudgetManagement> page);

  void export(BudgetManagementDTO budgetManagementDTO, HttpServletResponse response);
}
