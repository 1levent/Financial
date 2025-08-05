package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.Account;
import com.financial.business.entity.conveter.AccountStructMapper;
import com.financial.business.entity.dto.AccountDTO;
import com.financial.business.mapper.AccountMapper;
import com.financial.business.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户表 服务实现类
 * </p>
 *
 * @author xinyi
 * @since 2025-04-09
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

  @Resource
  private AccountStructMapper accountStructMapper;

  @Override
  public IPage<AccountDTO> list(AccountDTO dto, Page<Account> page) {
    Account entity = accountStructMapper.toEntity(dto);
    QueryWrapper<Account> queryWrapper = new QueryWrapper<>(entity);
    Page<Account> result = page(page, queryWrapper);
    return result.convert(accountStructMapper::toDto);
  }

  @Override
  public void export(AccountDTO accountDTO, HttpServletResponse response) {
    QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
//    if (accountDTO.getIds() != null && !accountDTO.getIds().isEmpty()) {
//      queryWrapper.in("id", accountDTO.getIds());
//    }
    Page<Account> page = new Page<>(1, Integer.MAX_VALUE);
    IPage<Account> result = page(page, queryWrapper);
    // 转化为dtoList
    List<AccountDTO> dtoList = accountStructMapper.toDtoList(result.getRecords());
    try {
      FastExcel.write(response.getOutputStream(), AccountDTO.class)
          .sheet("账户数据")
          .doWrite(dtoList);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 根据账户编号获取账户信息
   * @param accountNo
   * @return
   */
  @Override
  public Account getByAccountNo(String accountNo) {
    QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("account_no", accountNo);
    queryWrapper.eq("user_id", SecurityUtils.getUserId());
    return getOne(queryWrapper);
  }

  /**
   * 调整账户金额
   * @param accountNo 账户编号
   * @param amount 账户金额
   * @return 是否成功
   */
  @Override
  public boolean adjustAmount(String accountNo, String op,BigDecimal amount) {
    Account account = getByAccountNo(accountNo);
    if (account == null) {
      return false;
    }
    if ("+".equals(op)) {
      account.setAmount(account.getAmount().add(amount));
    } else if ("-".equals(op)) {
      if (account.getAmount().compareTo(amount) < 0) {
        return false;
      }
      account.setAmount(account.getAmount().subtract(amount));
    } else {
      return false;
    }
    return saveOrUpdate(account);
  }
}
