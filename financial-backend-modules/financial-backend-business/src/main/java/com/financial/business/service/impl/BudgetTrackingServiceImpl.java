package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.BudgetManagement;
import com.financial.business.entity.BudgetTracking;
import com.financial.business.entity.conveter.BudgetTrackingStructMapper;
import com.financial.business.entity.dto.BudgetTrackingDTO;
import com.financial.business.mapper.BudgetTrackingMapper;
import com.financial.business.service.IBudgetManagementService;
import com.financial.business.service.IBudgetTrackingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BudgetTrackingServiceImpl extends ServiceImpl<BudgetTrackingMapper, BudgetTracking> implements IBudgetTrackingService {

    @Resource
    private BudgetTrackingStructMapper budgetTrackingStructMapper;

    @Resource
    private IBudgetManagementService budgetManagementService;

    @Override
    public IPage<BudgetTrackingDTO> list(BudgetTrackingDTO dto, Page<BudgetTracking> page) {
        BudgetTracking entity = budgetTrackingStructMapper.toEntity(dto);
        QueryWrapper<BudgetTracking> queryWrapper = new QueryWrapper<>(entity);
        Page<BudgetTracking> result = page(page, queryWrapper);
        return result.convert(budgetTrackingStructMapper::toDto);
    }

    /**
     * 从预算管理表中获取信息
     * @param budgetId
     * @return
     */
    @Override
    public void getInfoByBudgetId(Long budgetId,BudgetTrackingDTO dto) {
        BudgetManagement budget = budgetManagementService.getById(budgetId);
        dto.setName(budget.getName());

    }

}