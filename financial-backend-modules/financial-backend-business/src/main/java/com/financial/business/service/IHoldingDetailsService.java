package com.financial.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.HoldingDetails;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.business.entity.dto.HoldingDetailsDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 持仓明细表 服务类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
public interface IHoldingDetailsService extends IService<HoldingDetails> {
  public IPage<HoldingDetailsDTO> list(HoldingDetailsDTO dto, Page<HoldingDetails> page);
  public void export(HoldingDetailsDTO dto, HttpServletResponse response);
}
