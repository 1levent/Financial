package com.financial.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.ProfitRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.business.entity.dto.ProfitRecordsDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 收益记录表 服务类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
public interface IProfitRecordsService extends IService<ProfitRecords> {
  public IPage<ProfitRecordsDTO> list(ProfitRecordsDTO dto, Page<ProfitRecords> page);
  public void export(ProfitRecordsDTO dto, HttpServletResponse response);
}
