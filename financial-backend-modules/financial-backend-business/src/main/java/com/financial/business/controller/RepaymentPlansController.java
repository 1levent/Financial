package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.business.entity.RepaymentPlans;
import com.financial.business.entity.conveter.RepaymentPlansStructMapper;
import com.financial.business.entity.dto.RepaymentPlansDTO;
import com.financial.business.service.IRepaymentPlansService;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.TableDataInfo;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.security.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 获取还款计划列表
     */
    @Operation(summary = "获取还款计划列表")
    @RequiresPermissions("business:repaymentPlans:list")
    @GetMapping("/list")
    public TableDataInfo list(RepaymentPlansDTO repaymentPlansDTO) {
        startPage();
        RepaymentPlans repaymentPlans = repaymentPlansStructMapper.toEntity(repaymentPlansDTO);
        List<RepaymentPlans> list = repaymentPlansService.list(new QueryWrapper<>(repaymentPlans));
        return getDataTable(repaymentPlansStructMapper.toDtoList(list));
    }

    /**
     * 根据还款计划编号获取详细信息
     */
    @Operation(summary = "根据还款计划编号获取详细信息")
    @RequiresPermissions("business:repaymentPlans:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(repaymentPlansService.getById(id));
    }

    /**
     * 新增还款计划
     */
    @Operation(summary = "新增还款计划")
    @RequiresPermissions("business:repaymentPlans:add")
    @Log(title = "还款计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody RepaymentPlans repaymentPlans) {
        return toAjax(repaymentPlansService.save(repaymentPlans));
    }

    /**
     * 修改还款计划
     */
    @Operation(summary = "修改还款计划")
    @RequiresPermissions("business:repaymentPlans:edit")
    @Log(title = "还款计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody RepaymentPlans repaymentPlans) {
        return toAjax(repaymentPlansService.updateById(repaymentPlans));
    }

    /**
     * 删除还款计划
     */
    @Operation(summary = "删除还款计划")
    @RequiresPermissions("business:repaymentPlans:remove")
    @Log(title = "还款计划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(repaymentPlansService.removeBatchByIds(Arrays.asList(ids)));
    }
}