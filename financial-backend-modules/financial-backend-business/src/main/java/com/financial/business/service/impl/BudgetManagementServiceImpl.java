package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.BudgetManagement;
import com.financial.business.entity.conveter.BudgetManagementStructMapper;
import com.financial.business.entity.dto.BudgetManagementDTO;
import com.financial.business.mapper.BudgetManagementMapper;
import com.financial.business.service.IBudgetManagementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BudgetManagementServiceImpl extends ServiceImpl<BudgetManagementMapper, BudgetManagement> implements IBudgetManagementService {

    @Resource
    private BudgetManagementStructMapper budgetManagementStructMapper;

    @Override
    public IPage<BudgetManagementDTO> list(BudgetManagementDTO dto, Page<BudgetManagement> page) {
        BudgetManagement entity = budgetManagementStructMapper.toEntity(dto);
        System.out.println("预算分页查询条件："+entity);
        QueryWrapper<BudgetManagement> queryWrapper = new QueryWrapper<>(entity);

//        if (dto.getStartDate() != null && dto.getEndDate() != null) {
//            queryWrapper.between("date", dto.getStartDate(), dto.getEndDate());
//        }
        Page<BudgetManagement> result = page(page, queryWrapper);
        return result.convert(budgetManagementStructMapper::toDto);
    }

    @Override
    public void export(BudgetManagementDTO dto, HttpServletResponse response) {
        QueryWrapper<BudgetManagement> queryWrapper = new QueryWrapper<>();
        if (dto.getIds() != null && !dto.getIds().isEmpty()) {
            queryWrapper.in("id", dto.getIds());
        }
        Page<BudgetManagement> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<BudgetManagement> result = page(page, queryWrapper);
        List<BudgetManagementDTO> dtoList = budgetManagementStructMapper.toDtoList(result.getRecords());
        try {
            FastExcel.write(response.getOutputStream(), BudgetManagementDTO.class)
                    .registerWriteHandler(new AutoColumnWidthStyle())
                    .sheet("预算管理")
                    .doWrite(dtoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}