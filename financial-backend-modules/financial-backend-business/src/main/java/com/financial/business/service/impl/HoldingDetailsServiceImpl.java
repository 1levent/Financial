package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.HoldingDetails;
import com.financial.business.entity.conveter.HoldingDetailsStructMapper;
import com.financial.business.entity.dto.HoldingDetailsDTO;
import com.financial.business.mapper.HoldingDetailsMapper;
import com.financial.business.service.IHoldingDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HoldingDetailsServiceImpl extends ServiceImpl<HoldingDetailsMapper, HoldingDetails> implements IHoldingDetailsService {

    @Resource
    private HoldingDetailsStructMapper holdingDetailsStructMapper;

    @Override
    public IPage<HoldingDetailsDTO> list(HoldingDetailsDTO dto, Page<HoldingDetails> page) {
        HoldingDetails entity = holdingDetailsStructMapper.toEntity(dto);
        QueryWrapper<HoldingDetails> queryWrapper = new QueryWrapper<>(entity);

        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            queryWrapper.between("date", dto.getStartDate(), dto.getEndDate());
        }
        Page<HoldingDetails> result = page(page, queryWrapper);
        return result.convert(holdingDetailsStructMapper::toDto);
    }

    @Override
    public void export(HoldingDetailsDTO dto, HttpServletResponse response) {
        QueryWrapper<HoldingDetails> queryWrapper = new QueryWrapper<>();
//        if (dto.getIds() != null && !dto.getIds().isEmpty()) {
//            queryWrapper.in("id", dto.getIds());
//        }
        Page<HoldingDetails> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<HoldingDetails> result = page(page, queryWrapper);
        List<HoldingDetailsDTO> dtoList = holdingDetailsStructMapper.toDtoList(result.getRecords());
        try {
            FastExcel.write(response.getOutputStream(), HoldingDetailsDTO.class)
                    .registerWriteHandler(new AutoColumnWidthStyle())
                    .sheet("持仓明细")
                    .doWrite(dtoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过产品代码来获取持仓明细
     * @param code
     * @return
     */
    @Override
    public HoldingDetails getByCode(String code) {
        QueryWrapper<HoldingDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        if (getOne(queryWrapper) != null) {
            return getOne(queryWrapper);
        }
        return null;
    }

    /**
     * 通过交易份额获取交易价格
     * @param shares
     * @return
     */
    @Override
    public BigDecimal getTradePrice(String code,BigDecimal shares) {
        //todo 先简单实现，后续再修改
        HoldingDetails holdingDetails = getByCode(code);
        return null;
    }
}