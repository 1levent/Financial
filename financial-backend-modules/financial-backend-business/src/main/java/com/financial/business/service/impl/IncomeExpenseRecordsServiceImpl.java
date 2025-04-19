package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.IncomeExpenseRecords;
import com.financial.business.entity.conveter.IncomeExpenseRecordsStructMapper;
import com.financial.business.entity.dto.IncomeExpenseRecordsDTO;
import com.financial.business.mapper.IncomeExpenseRecordsMapper;
import com.financial.business.service.IIncomeExpenseRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

  /**
   * 导出数据
   * @param incomeExpenseRecordsDTO 查询条件，主要是获取id集合
   * @param response 请求响应
   */
  @Override
  public void export(IncomeExpenseRecordsDTO incomeExpenseRecordsDTO,
      HttpServletResponse response) {
    QueryWrapper<IncomeExpenseRecords> queryWrapper = new QueryWrapper<>();
    if (incomeExpenseRecordsDTO.getIds() != null && !incomeExpenseRecordsDTO.getIds().isEmpty()) {
      queryWrapper.in("id", incomeExpenseRecordsDTO.getIds());
    }
    Page<IncomeExpenseRecords> page = new Page<>(1, Integer.MAX_VALUE);
    IPage<IncomeExpenseRecords> result = page(page, queryWrapper);
    //转化为dtoList
    List<IncomeExpenseRecordsDTO> dtoList = incomeExpenseRecordsStructMapper.toDtoList(
        result.getRecords());
    try {
      FastExcel.write(response.getOutputStream(), IncomeExpenseRecordsDTO.class)
//          .registerWriteHandler(new AutoColumnWidthStyle())
          .sheet("收支记录")
          .doWrite(dtoList);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
