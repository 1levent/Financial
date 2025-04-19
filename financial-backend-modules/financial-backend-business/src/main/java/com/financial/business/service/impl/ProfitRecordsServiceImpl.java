package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.ProfitRecords;
import com.financial.business.entity.conveter.ProfitRecordsStructMapper;
import com.financial.business.entity.dto.ProfitRecordsDTO;
import com.financial.business.mapper.ProfitRecordsMapper;
import com.financial.business.service.IProfitRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProfitRecordsServiceImpl extends ServiceImpl<ProfitRecordsMapper, ProfitRecords> implements IProfitRecordsService {

    @Resource
    private ProfitRecordsStructMapper profitRecordsStructMapper;

    @Override
    public IPage<ProfitRecordsDTO> list(ProfitRecordsDTO dto, Page<ProfitRecords> page) {
        ProfitRecords entity = profitRecordsStructMapper.toEntity(dto);
        QueryWrapper<ProfitRecords> queryWrapper = new QueryWrapper<>(entity);

//        if (dto.getStartDate() != null && dto.getEndDate() != null) {
//            queryWrapper.between("date", dto.getStartDate(), dto.getEndDate());
//        }
        Page<ProfitRecords> result = page(page, queryWrapper);
        return result.convert(profitRecordsStructMapper::toDto);
    }

    @Override
    public void export(ProfitRecordsDTO dto, HttpServletResponse response) {
        QueryWrapper<ProfitRecords> queryWrapper = new QueryWrapper<>();
//        if (dto.getIds() != null && !dto.getIds().isEmpty()) {
//            queryWrapper.in("id", dto.getIds());
//        }
        Page<ProfitRecords> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<ProfitRecords> result = page(page, queryWrapper);
        List<ProfitRecordsDTO> dtoList = profitRecordsStructMapper.toDtoList(result.getRecords());
        try {
            FastExcel.write(response.getOutputStream(), ProfitRecordsDTO.class)
                    .registerWriteHandler(new AutoColumnWidthStyle())
                    .sheet("收益记录")
                    .doWrite(dtoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}