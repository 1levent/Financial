package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.HoldingDetails;
import com.financial.business.entity.ProfitRecords;
import com.financial.business.entity.conveter.ProfitRecordsStructMapper;
import com.financial.business.entity.dto.data.FundDaily;
import com.financial.business.entity.dto.ProfitRecordsDTO;
import com.financial.business.mapper.ProfitRecordsMapper;
import com.financial.business.service.IHoldingDetailsService;
import com.financial.business.service.IProfitRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.business.service.data.AkShareDataService;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import com.financial.common.mq.service.MessagePublisher;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProfitRecordsServiceImpl extends ServiceImpl<ProfitRecordsMapper, ProfitRecords> implements IProfitRecordsService {

    @Resource
    private ProfitRecordsStructMapper profitRecordsStructMapper;

    @Resource
    private IHoldingDetailsService holdingDetailsService;

    private RestTemplate restTemplate = new RestTemplate();

    @Resource
    private MessagePublisher messagePublisher;

    @Resource
    private AkShareDataService akShareDataService;

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

    //创建定时任务，更新收益记录
    @XxlJob("updateProfitRecords")
    public void updateProfitRecords() {
        //从持仓明细表中获取所有持仓
        List<HoldingDetails> holdingDetails = holdingDetailsService.list();
        System.out.println("任务执行了一次");
        holdingDetails.forEach(item -> {
            System.out.println("正在更新" + item.getCode() + "的收益记录");
        });
        holdingDetails.forEach(item -> {
            String code  = item.getCode();
            List<FundDaily> fundDailyList = akShareDataService.getFondDailyData();
            System.out.println("获取到的数据:"+fundDailyList.size());
            //根据code,从fundDailyList中获取对应的增长率
            FundDaily fundDaily = fundDailyList.stream()
                .filter(fund -> fund.getFundCode().equals(code))
                .findFirst()
                .orElse(null);
            if (fundDaily != null) {
                //更新收益记录
                System.out.println("收益记录："+fundDaily);
                ProfitRecords profitRecords = new ProfitRecords();
                profitRecords.setHoldingDetailId(item.getId());
                profitRecords.setProfitTime(new Date());
                profitRecords.setUserId(item.getUserId());
                profitRecords.setChg(new BigDecimal(fundDaily.getDailyGrowthRate()));
                profitRecords.setProfitAmount(item.getQuantity().multiply(new BigDecimal(fundDaily.getDailyGrowthRate())));
                item.setProfitLoss(item.getProfitLoss().add(profitRecords.getProfitAmount()));
                item.setQuantity(item.getQuantity().add(profitRecords.getProfitAmount()));
                //保存持仓记录
                holdingDetailsService.updateById(item);
                //保存收益记录
                this.save(profitRecords);
                StringBuilder sb = new StringBuilder();
                sb.append("您的").append(item.getName())
                    .append("今日涨幅为："+fundDaily.getDailyGrowthRate())
                    .append("，今日收益为：")
                    .append(profitRecords.getProfitAmount()).append("元");
                System.out.println("向前端发送今日收益消息");
                messagePublisher.publishNotification(String.valueOf(item.getUserId()), sb.toString());
            }
        });
    }

}