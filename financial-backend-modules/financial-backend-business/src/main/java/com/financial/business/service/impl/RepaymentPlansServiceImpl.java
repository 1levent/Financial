package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.RepaymentPlans;
import com.financial.business.entity.conveter.RepaymentPlansStructMapper;
import com.financial.business.entity.dto.RepaymentPlansDTO;
import com.financial.business.mapper.RepaymentPlansMapper;
import com.financial.business.service.IRepaymentPlansService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RepaymentPlansServiceImpl extends ServiceImpl<RepaymentPlansMapper, RepaymentPlans> implements IRepaymentPlansService {

    @Resource
    private RepaymentPlansStructMapper repaymentPlansStructMapper;

    @Override
    public IPage<RepaymentPlansDTO> list(RepaymentPlansDTO dto, Page<RepaymentPlans> page) {
        RepaymentPlans entity = repaymentPlansStructMapper.toEntity(dto);
        QueryWrapper<RepaymentPlans> queryWrapper = new QueryWrapper<>(entity);

//        if (dto.getStartDate() != null && dto.getEndDate() != null) {
//            queryWrapper.between("date", dto.getStartDate(), dto.getEndDate());
//        }
        Page<RepaymentPlans> result = page(page, queryWrapper);
        return result.convert(repaymentPlansStructMapper::toDto);
    }

    @Override
    public void export(RepaymentPlansDTO dto, HttpServletResponse response) {
        QueryWrapper<RepaymentPlans> queryWrapper = new QueryWrapper<>();
//        if (dto.getIds() != null && !dto.getIds().isEmpty()) {
//            queryWrapper.in("id", dto.getIds());
//        }
        Page<RepaymentPlans> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<RepaymentPlans> result = page(page, queryWrapper);
        List<RepaymentPlansDTO> dtoList = repaymentPlansStructMapper.toDtoList(result.getRecords());
        try {
            FastExcel.write(response.getOutputStream(), RepaymentPlansDTO.class)
                    .registerWriteHandler(new AutoColumnWidthStyle())
                    .sheet("还款计划")
                    .doWrite(dtoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}