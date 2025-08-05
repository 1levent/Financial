package com.financial.business.service.impl;

import cn.idev.excel.FastExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.config.XxlJobUtil;
import com.financial.business.entity.LoanRecords;
import com.financial.business.entity.RepaymentPlans;
import com.financial.business.entity.conveter.RepaymentPlansStructMapper;
import com.financial.business.entity.dto.RepaymentPlansDTO;
import com.financial.business.entity.dto.statistic.RepaymentBarDTO;
import com.financial.business.entity.param.XxlJobInfo;
import com.financial.business.mapper.RepaymentPlansMapper;
import com.financial.business.service.ILoanRecordsService;
import com.financial.business.service.IRepaymentPlansService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.common.core.utils.excel.AutoColumnWidthStyle;
import com.financial.common.security.utils.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RepaymentPlansServiceImpl extends ServiceImpl<RepaymentPlansMapper, RepaymentPlans> implements IRepaymentPlansService {

    @Resource
    private RepaymentPlansStructMapper repaymentPlansStructMapper;

    @Resource
    private ILoanRecordsService loanRecordsService;

    @Resource
    private XxlJobUtil xxlJobUtil;

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
        dtoList.forEach(item -> {
            item.setLoanName(loanRecordsService.getById(item.getLoanRecordId()).getLoanName());
        });
        try {
            FastExcel.write(response.getOutputStream(), RepaymentPlansDTO.class)
                    .registerWriteHandler(new AutoColumnWidthStyle())
                    .sheet("还款计划")
                    .doWrite(dtoList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 计算还款进度
     * @param loanRecordId 借贷id
     * @return 还款进度
     */
    @Override
    public double calculateRepaymentProgress(RepaymentPlans repaymentPlans, Long loanRecordId) {
        // 判空处理
        if (repaymentPlans == null || loanRecordId == null) {
            return 0;
        }

        // 获取借贷记录
        LoanRecords loan = loanRecordsService.getById(loanRecordId);
        if (loan == null) {
            return 0;
        }

        // 计算总还款金额（本金 + 利息）
        BigDecimal totalRepaymentAmount = loan.getPrincipal().add(loan.getTotalInterest());

        // 如果总还款金额为0，返回0
        if (BigDecimal.ZERO.compareTo(totalRepaymentAmount) == 0) {
            return 0;
        }

        // 计算还款进度
        return repaymentPlans.getRepaymentTotalAmount()
                .divide(totalRepaymentAmount, 2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 创建定时任务
     * @param repaymentPlans 还款计划
     */
    @Override
    public void createJob(RepaymentPlans repaymentPlans) {
        String cron = getCron(repaymentPlans);
        if (cron != null && !cron.isEmpty()) {
            //创建定时任务
            XxlJobInfo jobInfo = new XxlJobInfo();
            jobInfo.setJobGroup(2);
            //用id作为任务描述
            jobInfo.setJobDesc(repaymentPlans.getId().toString());
            //设置任务参数
            jobInfo.setExecutorParam(repaymentPlans.getId().toString());
            jobInfo.setAuthor("admin");
            jobInfo.setScheduleType("CRON");
            jobInfo.setScheduleConf(cron);
            jobInfo.setMisfireStrategy("DO_NOTHING");
            jobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
            jobInfo.setExecutorRouteStrategy("FIRST");
            jobInfo.setExecutorTimeout(0);
            jobInfo.setExecutorFailRetryCount(0);

            //设置GLUE_JAVA模式专属配置
            jobInfo.setGlueType("GLUE_GROOVY");
            String code = "";
            try{
                code = new String(Files.readAllBytes(Paths.get("D:\\financial\\Financial\\financial-backend-modules\\financial-backend-business\\src\\main\\java\\com\\financial\\business\\job\\RepaymentGlueJobHandler.java")), StandardCharsets.UTF_8);
            }catch (IOException e){
                e.printStackTrace();
            }

            jobInfo.setGlueSource(code);
            jobInfo.setGlueRemark("GLUE代码初始化");
            jobInfo.setExecutorHandler("");

            //创建
            String jobId = xxlJobUtil.add(jobInfo);
            System.out.println("启动任务id");
            //返回任务id
            if (jobId != null && !jobId.isEmpty()) {
                //启动任务
                xxlJobUtil.start(Integer.parseInt(jobId));
                repaymentPlans.setJobId(Integer.parseInt(jobId));
                updateById(repaymentPlans);
            }
        }
    }

    @Override
    public List<RepaymentBarDTO> getStatistics(RepaymentPlans repaymentPlans) {
        List<RepaymentBarDTO> repaymentBarDTOS = new ArrayList<>();
        //先获取全部的还款计划
        List<RepaymentPlans> repaymentPlansList = list(new QueryWrapper<>(repaymentPlans));
        repaymentPlansList.forEach(item -> {
            RepaymentBarDTO repaymentBarDTO = new RepaymentBarDTO();
            LoanRecords records = loanRecordsService.getById(item.getLoanRecordId());
            repaymentBarDTO.setName(records.getLoanName());
            repaymentBarDTO.setTotal(records.getPrincipal().add(records.getTotalInterest()));
            repaymentBarDTO.setPaid(item.getRepaymentTotalAmount());
            repaymentBarDTOS.add(repaymentBarDTO);
        });
        return repaymentBarDTOS;
    }

    @Override
    public void removeJob(Long[] ids) {
        for (Long id : ids) {
            RepaymentPlans repaymentPlans = getById(id);
            if (repaymentPlans != null) {
                //删除定时任务
                xxlJobUtil.remove(repaymentPlans.getJobId());
            }
        }
    }

    @Override
    public void updateJob(RepaymentPlans repaymentPlans) {
        String cron = getCron(repaymentPlans);
        xxlJobUtil.update(repaymentPlans.getJobId(), cron);
    }

    public String getCron(RepaymentPlans repaymentPlans) {
        String period = repaymentPlans.getPeriod();
        // 获取还款日期的年月日信息（假设repaymentDate是LocalDate类型）
        int dayOfMonth = repaymentPlans.getRepaymentDate().getDayOfMonth();
        int month = repaymentPlans.getRepaymentDate().getMonthValue();
        DayOfWeek dayOfWeek = repaymentPlans.getRepaymentDate().getDayOfWeek();

        String cron = "";
        switch (period) {
            case "每日":
                cron = "0 0 0 * * ?"; // 每天0点
                break;
            case "每周":
                // 转换为cron的星期格式（1=周日,2=周一...7=周六）
                int cronWeek = dayOfWeek.getValue() % 7 + 1;
                cron = String.format("0 0 0 ? * %d", cronWeek);
                break;
            case "每月":
                cron = String.format("0 0 0 %d * ?", dayOfMonth); // 每月指定日
                break;
            case "每年":
                cron = String.format("0 0 0 %d %d ?", dayOfMonth, month); // 每年指定月日
                break;
            default:
                throw new IllegalArgumentException("不支持的周期类型");
        }
        return cron;
    }
}