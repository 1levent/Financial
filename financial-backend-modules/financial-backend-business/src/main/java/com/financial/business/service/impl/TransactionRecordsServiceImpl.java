package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.TransactionRecords;
import com.financial.business.entity.conveter.TransactionRecordsStructMapper;
import com.financial.business.entity.dto.TransactionRecordsDTO;
import com.financial.business.mapper.TransactionRecordsMapper;
import com.financial.business.service.ITransactionRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionRecordsServiceImpl extends ServiceImpl<TransactionRecordsMapper, TransactionRecords> implements ITransactionRecordsService {

    @Resource
    private TransactionRecordsStructMapper transactionRecordsStructMapper;

    @Override
    public IPage<TransactionRecordsDTO> list(TransactionRecordsDTO dto, Page<TransactionRecords> page) {
        TransactionRecords entity = transactionRecordsStructMapper.toEntity(dto);
        QueryWrapper<TransactionRecords> queryWrapper = new QueryWrapper<>(entity);

//        if (dto.getStartDate() != null && dto.getEndDate() != null) {
//            queryWrapper.between("date", dto.getStartDate(), dto.getEndDate());
//        }
        Page<TransactionRecords> result = page(page, queryWrapper);
        return result.convert(transactionRecordsStructMapper::toDto);
    }

    @Override
    public void export(TransactionRecordsDTO dto, HttpServletResponse response) {
        QueryWrapper<TransactionRecords> queryWrapper = new QueryWrapper<>();
//        if (dto.getIds() != null && !dto.getIds().isEmpty()) {
//            queryWrapper.in("id", dto.getIds());
//        }
        Page<TransactionRecords> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<TransactionRecords> result = page(page, queryWrapper);
        List<TransactionRecordsDTO> dtoList = transactionRecordsStructMapper.toDtoList(result.getRecords());
        try {
            FastExcel.write(response.getOutputStream(), TransactionRecordsDTO.class)
                    .registerWriteHandler(new AutoColumnWidthStyle())
                    .sheet("交易记录")
                    .doWrite(dtoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}