package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.IncomeExpenseRecords;
import com.financial.business.entity.conveter.IncomeExpenseRecordsStructMapper;
import com.financial.business.entity.dto.statistic.IncomeExpenseCategoryDataDTO;
import com.financial.business.entity.dto.statistic.IncomeExpenseLineDataDTO;
import com.financial.business.entity.dto.IncomeExpenseRecordsDTO;
import com.financial.business.entity.dto.statistic.IncomeExpenseStatisticsDTO;
import com.financial.business.mapper.IncomeExpenseRecordsMapper;
import com.financial.business.service.IIncomeExpenseRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  @Override
  public IncomeExpenseStatisticsDTO calculateStatistics(List<IncomeExpenseRecords> list) {
    IncomeExpenseStatisticsDTO statisticsDTO = new IncomeExpenseStatisticsDTO();

    // 计算总收入、总支出和结余
    BigDecimal incomeTotal = list.stream()
        .filter(item -> "收入".equals(item.getType()))
        .map(IncomeExpenseRecords::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    BigDecimal expenseTotal = list.stream()
        .filter(item -> "支出".equals(item.getType()))
        .map(IncomeExpenseRecords::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    BigDecimal remain = incomeTotal.subtract(expenseTotal);
    
    statisticsDTO.setIncomeTotal(incomeTotal);
    statisticsDTO.setExpenseTotal(expenseTotal);
    statisticsDTO.setRemain(remain);

    // 按日期和类型分组，生成每日的收入和支出两条独立记录
    Map<LocalDate, Map<String, IncomeExpenseLineDataDTO>> dateTypeMap = new HashMap<>();

    for (IncomeExpenseRecords record : list) {
      LocalDate date = record.getDate();
      String recordType = record.getType();

      // 为每个日期初始化收入和支出两条记录
      Map<String, IncomeExpenseLineDataDTO> typeMap = dateTypeMap.computeIfAbsent(date, k -> {
        Map<String, IncomeExpenseLineDataDTO> map = new HashMap<>(2);
        map.put("收入", new IncomeExpenseLineDataDTO(date, "收入", BigDecimal.ZERO));
        map.put("支出", new IncomeExpenseLineDataDTO(date, "支出", BigDecimal.ZERO));
        return map;
      });

      // 更新对应类型的金额
      IncomeExpenseLineDataDTO dto = typeMap.get(recordType);
      if (dto != null) {
        dto.setAmount(dto.getAmount().add(record.getAmount()));
      }
    }

    // 将嵌套Map转换为平铺列表，并排序
    List<IncomeExpenseLineDataDTO> lineDataList = new ArrayList<>();
    dateTypeMap.forEach((date, typeMap) -> {
      lineDataList.add(typeMap.get("收入"));
      lineDataList.add(typeMap.get("支出"));
    });
    lineDataList.sort(Comparator.comparing(IncomeExpenseLineDataDTO::getDate));

    statisticsDTO.setLineData(lineDataList);
    
    // 收集分类饼图数据
    Map<String, IncomeExpenseCategoryDataDTO> categoryDataMap = new HashMap<>();
    for (IncomeExpenseRecords record : list) {
        // 过滤掉收入记录
        if("收入".equals(record.getType())) {
          continue;
        }
        String category = record.getCategory();
        IncomeExpenseCategoryDataDTO categoryData = categoryDataMap.computeIfAbsent(category, k -> new IncomeExpenseCategoryDataDTO(category, record.getType(), BigDecimal.ZERO));
        categoryData.setAmount(categoryData.getAmount().add(record.getAmount()));
    }
    List<IncomeExpenseCategoryDataDTO> categoryDataList = new ArrayList<>(categoryDataMap.values());
    statisticsDTO.setCategoryData(categoryDataList);
    
    return statisticsDTO;
  }
}
