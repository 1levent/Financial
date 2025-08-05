package com.financial.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.business.entity.dto.AccountDTO;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * <p>
 * 账户表 服务类
 * </p>
 *
 * @author xinyi
 * @since 2025-04-09
 */
public interface IAccountService extends IService<Account> {

  public IPage<AccountDTO> list(AccountDTO dto, Page<Account> page);
  void export(AccountDTO accountDTO, HttpServletResponse response);
  Account getByAccountNo(String accountNo);
  boolean adjustAmount(String accountNo,String op, BigDecimal amount);
}
