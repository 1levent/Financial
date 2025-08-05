package com.financial.business.job;

import com.financial.business.entity.IncomeExpenseRecords;
import com.financial.business.entity.LoanRecords;
import com.financial.business.entity.RepaymentPlans;
import com.financial.business.service.IAccountService;
import com.financial.business.service.IIncomeExpenseRecordsService;
import com.financial.business.service.ILoanRecordsService;
import com.financial.business.service.IRepaymentPlansService;
import com.financial.common.mq.service.MessagePublisher;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import org.apache.logging.log4j.util.StringBuilders;
import org.springframework.stereotype.Service;

/**
 * 还款计划定时任务
 * @author xinyi
 */
@Service
public class RepaymentGlueJobHandler  extends IJobHandler {

    @Resource
    private IRepaymentPlansService repaymentPlansService;

    @Resource
    private IIncomeExpenseRecordsService incomeExpenseRecordsService;

    @Resource
    private ILoanRecordsService loanRecordsService;

    @Resource
    private IAccountService accountService;

    @Resource
    private MessagePublisher mmessagePublisher;
    @Override
    public void execute() throws Exception {
        //还款相当于新增一条支出记录
        IncomeExpenseRecords incomeExpenseRecords = new IncomeExpenseRecords();
        incomeExpenseRecords.setType("支出");
        incomeExpenseRecords.setCategory("还款");
        //获取还款计划id
        Long repaymentPlansId = Long.valueOf(Objects.requireNonNull(XxlJobHelper.getJobParam()));
        RepaymentPlans repaymentPlans = repaymentPlansService.getById(repaymentPlansId);
        //获取还款金额，转化成double
        incomeExpenseRecords.setAmount(repaymentPlans.getRepaymentAmount());
        incomeExpenseRecords.setAccountId(repaymentPlans.getAccountId());
        //账户也要扣钱
        accountService.adjustAmount(repaymentPlans.getAccountId(),"-",repaymentPlans.getRepaymentAmount());
        incomeExpenseRecords.setDate(LocalDate.now());
        incomeExpenseRecords.setDescription("自动还款");
        incomeExpenseRecords.setLoanId(repaymentPlans.getLoanRecordId());
        incomeExpenseRecords.setUserId(repaymentPlans.getUserId());
        incomeExpenseRecordsService.save(incomeExpenseRecords);
        //通知前端
        LoanRecords loanRecords = loanRecordsService.getById(repaymentPlans.getLoanRecordId());

        StringBuilder sb = new StringBuilder();
        sb.append("您的贷款[").append(loanRecords.getLoanName()).append("]已还:￥").append(repaymentPlans.getRepaymentAmount()).append("元，还剩￥").append(loanRecords.getPrincipal().add(loanRecords.getTotalInterest()).subtract(repaymentPlans.getRepaymentAmount())).append("元");
        mmessagePublisher.publishNotification(String.valueOf(incomeExpenseRecords.getUserId()), sb.toString());
        //增加一次还款
        repaymentPlans.setCount(repaymentPlans.getCount() + 1);
        repaymentPlans.setRepaymentTotalAmount(repaymentPlans.getRepaymentTotalAmount().add(repaymentPlans.getRepaymentAmount()));
        repaymentPlans.setRepaymentProgress(repaymentPlans.getRepaymentTotalAmount()
            .divide(loanRecords.getPrincipal().add(loanRecords.getTotalInterest()),2, RoundingMode.CEILING)
            .multiply(new BigDecimal(100))
        );
        repaymentPlansService.updateById(repaymentPlans);
    }
}
