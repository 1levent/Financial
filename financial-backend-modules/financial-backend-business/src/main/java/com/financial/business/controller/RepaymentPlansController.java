package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.config.XxlJobUtil;
import com.financial.business.entity.RepaymentPlans;
import com.financial.business.entity.conveter.RepaymentPlansStructMapper;
import com.financial.business.entity.dto.RepaymentPlansDTO;
import com.financial.business.entity.dto.statistic.RepaymentBarDTO;
import com.financial.business.entity.param.XxlJobInfo;
import com.financial.business.service.ILoanRecordsService;
import com.financial.business.service.IRepaymentPlansService;
import com.financial.common.core.domain.R;
import com.financial.common.core.utils.excel.ExcelUtils;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.PageResponse;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.security.annotation.RequiresPermissions;
import com.financial.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 还款计划表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/repaymentPlans")
@Tag(name = "还款计划表")
public class RepaymentPlansController extends BaseController {

    @Resource
    private IRepaymentPlansService repaymentPlansService;

    @Resource
    private RepaymentPlansStructMapper repaymentPlansStructMapper;

    @Resource
    private ILoanRecordsService loanRecordsService;

    @Resource
    private XxlJobUtil xxlJobUtil;

    /**
     * 获取还款计划列表
     */
    @Operation(summary = "获取还款计划列表")
//    @RequiresPermissions("business:repaymentPlans:list")
    @PostMapping("/list")
    public PageResponse<RepaymentPlansDTO> list(
        @RequestBody RepaymentPlansDTO repaymentPlansDTO,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<RepaymentPlans> page = new Page<>(pageNum, pageSize);
        repaymentPlansDTO.setUserId(SecurityUtils.getUserId());
        IPage<RepaymentPlansDTO> result = repaymentPlansService.list(repaymentPlansDTO, page);
        //添加借贷名称
        result.getRecords().forEach(item -> {
            item.setLoanName(loanRecordsService.getById(item.getLoanRecordId()).getLoanName());
        });
        return PageResponse.success((int) result.getCurrent(), (int) result.getSize(), result.getRecords(), result.getTotal());
    }

    @Operation(summary = "导出还款计划列表")
//    @RequiresPermissions("business:repaymentPlans:export")
    @Log(title = "还款计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RepaymentPlansDTO repaymentPlansDTO)
        throws IOException, UnsupportedEncodingException {
        repaymentPlansDTO.setUserId(SecurityUtils.getUserId());
        repaymentPlansService.export(repaymentPlansDTO, ExcelUtils.setFileName(response, "还款计划"));
    }

    /**
     * 根据还款计划编号获取详细信息
     */
    @Operation(summary = "根据还款计划编号获取详细信息")
//    @RequiresPermissions("business:repaymentPlans:query")
    @GetMapping(value = "/{id}")
    public R<RepaymentPlansDTO> getInfo(@PathVariable Long id) {
        RepaymentPlans repaymentPlans = repaymentPlansService.getById(id);
        if(repaymentPlans==null) {
            return R.fail("该记录不存在");
        }
        RepaymentPlansDTO repaymentPlansDTO = repaymentPlansStructMapper.toDto(repaymentPlans);
        repaymentPlansDTO.setLoanName(loanRecordsService.getById(repaymentPlansDTO.getLoanRecordId()).getLoanName());
        return R.ok(repaymentPlansDTO);
    }

    /**
     * 新增还款计划
     */
    @Operation(summary = "新增还款计划")
//    @RequiresPermissions("business:repaymentPlans:add")
    @Log(title = "还款计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody RepaymentPlansDTO repaymentPlansDTO) {
        repaymentPlansDTO.setUserId(SecurityUtils.getUserId());
        RepaymentPlans repaymentPlans = repaymentPlansStructMapper.toEntity(repaymentPlansDTO);
        repaymentPlans.setCount(0L);
        repaymentPlans.setRepaymentProgress(BigDecimal.ZERO);
        repaymentPlans.setRepaymentTotalAmount(BigDecimal.ZERO);
        //创建定时任务
        boolean flag = repaymentPlansService.save(repaymentPlans);
        //保存之后获得回填id
        repaymentPlansService.createJob(repaymentPlans);
        return toAjax(flag);
    }

    /**
     * 修改还款计划
     */
    @Operation(summary = "修改还款计划")
//    @RequiresPermissions("business:repaymentPlans:edit")
    @Log(title = "还款计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody RepaymentPlans repaymentPlans) {
        repaymentPlans.setUserId(SecurityUtils.getUserId());
        boolean flag = repaymentPlansService.updateById(repaymentPlans);
        RepaymentPlans plans = repaymentPlansService.getById(repaymentPlans.getId());
        //同步修改定时任务
        repaymentPlansService.updateJob(plans);
        return toAjax(flag);
    }

    /**
     * 删除还款计划
     */
    @Operation(summary = "删除还款计划")
//    @RequiresPermissions("business:repaymentPlans:remove")
    @Log(title = "还款计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        //删除还款计划之前，先移除对应的定时任务
        repaymentPlansService.removeJob(ids);
        return toAjax(repaymentPlansService.removeBatchByIds(Arrays.asList(ids)));
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取还款计划统计")
    public R<List<RepaymentBarDTO>> getStatistics() {
        RepaymentPlans repaymentPlans = new RepaymentPlans();
        repaymentPlans.setUserId(SecurityUtils.getUserId());
        List<RepaymentBarDTO> repaymentBarDTOS = repaymentPlansService.getStatistics(repaymentPlans);
        return R.ok(repaymentBarDTOS);
    }



    @GetMapping("/test")
    public void test(){
        logger.info("测试xxl-job");
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setJobDesc("测试任务6");
        xxlJobInfo.setScheduleType("CRON");
        xxlJobInfo.setScheduleConf("* * * * * ?");
        xxlJobInfo.setGlueType("BEAN");
        xxlJobInfo.setExecutorHandler("test");
        xxlJobInfo.setAuthor("xinyi");
        xxlJobInfo.setExecutorRouteStrategy("FIRST");
        xxlJobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        xxlJobInfo.setExecutorTimeout(0);
        xxlJobInfo.setExecutorFailRetryCount(0);
        xxlJobInfo.setMisfireStrategy("DO_NOTHING");
        xxlJobUtil.add(xxlJobInfo);
    }
}