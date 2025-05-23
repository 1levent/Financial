package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.business.entity.GoalPlanning;
import com.financial.business.entity.conveter.GoalPlanningStructMapper;
import com.financial.business.entity.dto.GoalPlanningDTO;
import com.financial.business.service.IGoalPlanningService;
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
 * 目标规划表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/goalPlanning")
@Tag(name = "目标规划表")
public class GoalPlanningController extends BaseController {

    @Resource
    private IGoalPlanningService goalPlanningService;

    @Resource
    private GoalPlanningStructMapper goalPlanningStructMapper;

    /**
     * 获取目标规划列表
     */
    @Operation(summary = "获取目标规划列表")
    @RequiresPermissions("business:goalPlanning:list")
    @GetMapping("/list")
    public TableDataInfo list(GoalPlanningDTO goalPlanningDTO) {
        startPage();
        GoalPlanning goalPlanning = goalPlanningStructMapper.toEntity(goalPlanningDTO);
        List<GoalPlanning> list = goalPlanningService.list(new QueryWrapper<>(goalPlanning));
        return getDataTable(goalPlanningStructMapper.toDtoList(list));
    }

    /**
     * 根据目标规划编号获取详细信息
     */
    @Operation(summary = "根据目标规划编号获取详细信息")
    @RequiresPermissions("business:goalPlanning:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(goalPlanningService.getById(id));
    }

    /**
     * 新增目标规划
     */
    @Operation(summary = "新增目标规划")
    @RequiresPermissions("business:goalPlanning:add")
    @Log(title = "目标规划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody GoalPlanning goalPlanning) {
        return toAjax(goalPlanningService.save(goalPlanning));
    }

    /**
     * 修改目标规划
     */
    @Operation(summary = "修改目标规划")
    @RequiresPermissions("business:goalPlanning:edit")
    @Log(title = "目标规划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody GoalPlanning goalPlanning) {
        return toAjax(goalPlanningService.updateById(goalPlanning));
    }

    /**
     * 删除目标规划
     */
    @Operation(summary = "删除目标规划")
    @RequiresPermissions("business:goalPlanning:remove")
    @Log(title = "目标规划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(goalPlanningService.removeBatchByIds(Arrays.asList(ids)));
    }
}