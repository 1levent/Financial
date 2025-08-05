package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.LoanRecords;
import com.financial.business.entity.conveter.LoanRecordsStructMapper;
import com.financial.business.entity.dto.LoanRecordsDTO;
import com.financial.business.entity.dto.statistic.LoanStatisticDTO;
import com.financial.business.mapper.LoanRecordsMapper;
import com.financial.business.service.ILoanRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LoanRecordsServiceImpl extends ServiceImpl<LoanRecordsMapper, LoanRecords> implements ILoanRecordsService {

    @Resource
    private LoanRecordsStructMapper loanRecordsStructMapper;

    @Override
    public IPage<LoanRecordsDTO> list(LoanRecordsDTO dto, Page<LoanRecords> page) {
        LoanRecords entity = loanRecordsStructMapper.toEntity(dto);
        QueryWrapper<LoanRecords> queryWrapper = new QueryWrapper<>(entity);
        Page<LoanRecords> result = page(page, queryWrapper);
        return result.convert(loanRecordsStructMapper::toDto);
    }

    @Override
    public void export(LoanRecordsDTO dto, HttpServletResponse response) {
        QueryWrapper<LoanRecords> queryWrapper = new QueryWrapper<>();
//        if (dto.getIds() != null && !dto.getIds().isEmpty()) {
//            queryWrapper.in("id", dto.getIds());
//        }
        Page<LoanRecords> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<LoanRecords> result = page(page, queryWrapper);
        List<LoanRecordsDTO> dtoList = loanRecordsStructMapper.toDtoList(result.getRecords());
        try {
            FastExcel.write(response.getOutputStream(), LoanRecordsDTO.class)
                    .registerWriteHandler(new AutoColumnWidthStyle())
                    .sheet("借贷记录")
                    .doWrite(dtoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BigDecimal calculateTotalInterest(LoanRecordsDTO loanRecordsDTO) {
        //根据本金、年利率、借款期限计算总利息，写一个方法来计算
        BigDecimal principal = loanRecordsDTO.getPrincipal();
        BigDecimal annualInterestRate = loanRecordsDTO.getAnnualInterestRate();
        int loanTerm = loanRecordsDTO.getLoanTerm();
        return principal.multiply(annualInterestRate)
          .divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP)
          .multiply(new BigDecimal(loanTerm));
    }

    @Override
    public LoanStatisticDTO calculateLoanStatistics(List<LoanRecords> list) {
        LoanStatisticDTO loanStatisticDTO = new LoanStatisticDTO();
        loanStatisticDTO.setTotalCount(list.size());
        loanStatisticDTO.setTotalBorrowed(list.stream().map(LoanRecords::getPrincipal).reduce(BigDecimal.ZERO, BigDecimal::add));
        loanStatisticDTO.setTotalInterest(list.stream().map(LoanRecords::getTotalInterest).reduce(BigDecimal.ZERO, BigDecimal::add));
        return loanStatisticDTO;
    }
}