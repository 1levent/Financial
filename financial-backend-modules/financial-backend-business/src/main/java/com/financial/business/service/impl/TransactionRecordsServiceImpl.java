package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.Account;
import com.financial.business.entity.HoldingDetails;
import com.financial.business.entity.IncomeExpenseRecords;
import com.financial.business.entity.TransactionRecords;
import com.financial.business.entity.conveter.TransactionRecordsStructMapper;
import com.financial.business.entity.dto.TransactionRecordsDTO;
import com.financial.business.entity.dto.data.FundBuyStatus;
import com.financial.business.entity.dto.data.FundDaily;
import com.financial.business.mapper.TransactionRecordsMapper;
import com.financial.business.service.IAccountService;
import com.financial.business.service.IHoldingDetailsService;
import com.financial.business.service.IIncomeExpenseRecordsService;
import com.financial.business.service.ITransactionRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.business.service.data.AkShareDataService;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.mq.service.MessagePublisher;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionRecordsServiceImpl extends ServiceImpl<TransactionRecordsMapper, TransactionRecords> implements ITransactionRecordsService {

    @Resource
    private TransactionRecordsStructMapper transactionRecordsStructMapper;

    @Resource
    private AkShareDataService akShareDataService;

    @Resource
    private IAccountService accountService;

    @Resource
    private MessagePublisher messagePublisher;

    @Resource
    private IHoldingDetailsService holdingDetailsService;

    @Resource
    private IIncomeExpenseRecordsService incomeExpenseRecordsService;

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


    @Override
    public AjaxResult saveTransaction(TransactionRecordsDTO transactionRecordsDTO) {
        List<FundBuyStatus> fundBuyStatusList = akShareDataService.getFundBuyStatus();
        //从中获取匹配的数据
        FundBuyStatus fundBuyStatus =fundBuyStatusList.stream()
            .filter(item  -> item.getCode().equals(transactionRecordsDTO.getAssetCode()))
            .findFirst()
            .orElse(null);
        if(fundBuyStatus == null) {
          return AjaxResult.error("产品不存在");
        }
        if("买入".equals(transactionRecordsDTO.getType())){
            Account account = accountService.getByAccountNo(transactionRecordsDTO.getAccountNo());
            if(transactionRecordsDTO.getPrice().compareTo(account.getAmount())>0){
                return AjaxResult.error("账户余额不足");
            }
            if("暂停申购".equals(fundBuyStatus.getBuyStatus())){
                return AjaxResult.error("产品已暂停申购，下一次开放时间："+fundBuyStatus.getNextOpenTime());
            }
            //如果购买的价格小于申购起点，则返回false
            if (transactionRecordsDTO.getPrice().compareTo(fundBuyStatus.getBuyBegin()) < 0){
                return AjaxResult.error("购买价格小于申购起点："+fundBuyStatus.getBuyBegin()+"元");
            }
            //如果购买的价格大于日累计限定金额，则返回false
            if(transactionRecordsDTO.getPrice().compareTo(fundBuyStatus.getBuyEnd()) > 0){
                return AjaxResult.error("购买价格高于限额："+fundBuyStatus.getBuyEnd()+"元");
            }
            //设置手续费，是%
            transactionRecordsDTO.setFee(fundBuyStatus.getFee());
            //设置交易价格减去手续费
            transactionRecordsDTO.setPrice(transactionRecordsDTO.getPrice().subtract(transactionRecordsDTO.getPrice().multiply(transactionRecordsDTO.getFee())));
            //先创建收支记录
            IncomeExpenseRecords incomeExpenseRecords = new IncomeExpenseRecords();
            incomeExpenseRecords.setUserId(transactionRecordsDTO.getUserId());
            incomeExpenseRecords.setAccountId(account.getAccountNo());
            incomeExpenseRecords.setAmount(transactionRecordsDTO.getPrice());
            incomeExpenseRecords.setCategory("买入");
            incomeExpenseRecords.setDate(LocalDate.now());
            incomeExpenseRecords.setDescription("买入基金");
            incomeExpenseRecords.setType("支出");
            incomeExpenseRecordsService.save(incomeExpenseRecords);
            messagePublisher.publishNotification(String.valueOf(transactionRecordsDTO.getUserId()), "您已购买"+fundBuyStatus.getName()+"基金，购买金额为："+transactionRecordsDTO.getPrice()+"元，已创建对应支出记录");
            //账户扣款
            accountService.adjustAmount(transactionRecordsDTO.getAccountNo(),"-",transactionRecordsDTO.getPrice());
        }else{
            if("暂停赎回".equals(fundBuyStatus.getSellStatus())){
                return AjaxResult.error("产品已暂停赎回，下一次开放时间："+fundBuyStatus.getNextOpenTime());
            }
        }
        TransactionRecords transactionRecords = transactionRecordsStructMapper.toEntity(transactionRecordsDTO);
        //获取交易时间，如果是在三点之前，则获取今日净值，若是之后，则获取后一个交易日的净值
        if(transactionRecordsDTO.isBeforeThree()){
            transactionRecords.setTransactionTime(akShareDataService.getNextTradeDay());
        }else{
            transactionRecords.setTransactionTime(akShareDataService.getNextTradeDay(LocalDate.now()));
        }
        return AjaxResult.success(save(transactionRecords));
    }

    /**
     * 创建一个定时任务，晚上10点执行，将交易记录保存到数据库中
     */
    @XxlJob("todayTransactionJob")
    public void todayTransactionJob(){
        //获取交易记录，如果交易日期在今天，则执行
        List<TransactionRecords> transactionRecordsList = list(new QueryWrapper<TransactionRecords>().eq("transaction_time", LocalDate.now()));
        List<FundBuyStatus> fundBuyStatusList = akShareDataService.getFundBuyStatus();
        transactionRecordsList.forEach(item -> {
            FundBuyStatus fundBuyStatus = fundBuyStatusList.stream()
                .filter(fundBuyStatus1 -> fundBuyStatus1.getCode().equals(item.getAssetCode()))
                .findFirst()
                .orElse(null);
            //1.确定加仓金额与净值
            //2.计算新增份额
            //3.更新总份额与持仓成本
            //更新对应的持仓记录
            HoldingDetails product = holdingDetailsService.getByCode(item.getAssetCode());
            if(fundBuyStatus != null){
                if("买入".equals(item.getType())){
                    //计算份数=价格÷基金净值
                    BigDecimal shares = item.getPrice().divide(fundBuyStatus.getEquity(), 2, RoundingMode.HALF_UP);
                    item.setShares(shares);
                    if(product != null){
                        //更新当前净值
                        product.setEquity(fundBuyStatus.getEquity());
                        //更新份额
                        product.setShares(product.getShares().add(shares));
                        //更新单位成本
                        product.setUnitCost(product.getCost().add(item.getPrice()).divide(product.getShares(), 2, RoundingMode.HALF_UP));
                        //更新持仓成本
                        product.setCost(product.getUnitCost().multiply(product.getShares()));
                    }else {
                        //新建一条持仓记录
                        System.out.println("新建持仓记录");
                        product = new HoldingDetails();
                        product.setAccountNo(item.getAccountNo());
                        product.setCode(item.getAssetCode());
                        product.setName(fundBuyStatus.getName());
                        product.setType("基金");
                        product.setStartDate(LocalDate.now());
                        product.setEquity(fundBuyStatus.getEquity());
                        product.setUnitCost(product.getEquity());
                        product.setShares(shares);
                        product.setCost(item.getPrice());
                    }
                }else{
                    //计算卖出金额
                    BigDecimal price = item.getShares().multiply(fundBuyStatus.getEquity());
                    //更新持仓成本
                    if(product != null){
                        //更新份额
                        product.setShares(product.getShares().subtract(item.getShares()));
                        product.setUnitCost(product.getCost().subtract(price).divide(product.getShares(), 2, RoundingMode.HALF_UP));
                        product.setCost(product.getUnitCost().multiply(product.getShares()));
                    }
                    //todo 假设手续费为0,收益到账
                    //生成一条收入记录
                    IncomeExpenseRecords incomeExpenseRecords = new IncomeExpenseRecords();
                    incomeExpenseRecords.setUserId(item.getUserId());
                    incomeExpenseRecords.setAccountId(item.getAccountNo());
                    incomeExpenseRecords.setAmount(price);
                    incomeExpenseRecords.setCategory("卖出");
                    incomeExpenseRecords.setDate(LocalDate.now());
                    incomeExpenseRecords.setDescription("卖出"+item.getAssetCode()+"基金");
                    incomeExpenseRecords.setType("收入");
                    incomeExpenseRecordsService.save(incomeExpenseRecords);
                    accountService.adjustAmount(item.getAccountNo(),"+",price);
                }
            }
            holdingDetailsService.saveOrUpdate(product);
        });
    }
}