package com.financial.business.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.IncomeExpenseRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.business.entity.dto.IncomeExpenseRecordsDTO;

/**
 * <p>
 * 收支记录表 服务类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
public interface IIncomeExpenseRecordsService extends IService<IncomeExpenseRecords> {

  public IPage<IncomeExpenseRecordsDTO> list(IncomeExpenseRecordsDTO dto, Page<IncomeExpenseRecords> page);
}
