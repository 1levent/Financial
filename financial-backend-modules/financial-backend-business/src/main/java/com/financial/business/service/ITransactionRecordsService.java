package com.financial.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.TransactionRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.business.entity.dto.TransactionRecordsDTO;
import com.financial.common.core.web.domain.AjaxResult;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 交易记录表 服务类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
public interface ITransactionRecordsService extends IService<TransactionRecords> {
  public IPage<TransactionRecordsDTO> list(TransactionRecordsDTO dto, Page<TransactionRecords> page);
  public void export(TransactionRecordsDTO dto, HttpServletResponse response);

  AjaxResult saveTransaction(TransactionRecordsDTO transactionRecordsDTO);
}
