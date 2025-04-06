package com.financial.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.IncomeExpenseRecords;
import com.financial.business.entity.conveter.IncomeExpenseRecordsStructMapper;
import com.financial.business.entity.dto.IncomeExpenseRecordsDTO;
import com.financial.business.mapper.IncomeExpenseRecordsMapper;
import com.financial.business.service.IIncomeExpenseRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收支记录表 服务实现类
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@Service
public class IncomeExpenseRecordsServiceImpl extends ServiceImpl<IncomeExpenseRecordsMapper, IncomeExpenseRecords> implements IIncomeExpenseRecordsService {

  @Resource
  private IncomeExpenseRecordsStructMapper incomeExpenseRecordsStructMapper;

  @Override
  public IPage<IncomeExpenseRecordsDTO> list(IncomeExpenseRecordsDTO dto, Page<IncomeExpenseRecords> page) {

    IncomeExpenseRecords entity = incomeExpenseRecordsStructMapper.toEntity(dto);
    QueryWrapper<IncomeExpenseRecords> queryWrapper = new QueryWrapper<>(entity);

    //如果需要对日期进行筛选，则获取日期的开始和结束日期
    if (dto.getStartDate() != null && dto.getEndDate() != null) {
      queryWrapper.between("date", dto.getStartDate(), dto.getEndDate());
    }
    Page<IncomeExpenseRecords>  result = page(page, queryWrapper);
    return result.convert(incomeExpenseRecordsStructMapper::toDto);
  }
}
