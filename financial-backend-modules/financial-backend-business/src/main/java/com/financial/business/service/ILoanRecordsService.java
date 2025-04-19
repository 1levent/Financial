package com.financial.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.LoanRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.business.entity.dto.LoanRecordsDTO;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 借贷记录表 服务类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
public interface ILoanRecordsService extends IService<LoanRecords> {

  public IPage<LoanRecordsDTO> list(LoanRecordsDTO dto, Page<LoanRecords> page);

  void export(LoanRecordsDTO loanRecordsDTO, HttpServletResponse response);
}
