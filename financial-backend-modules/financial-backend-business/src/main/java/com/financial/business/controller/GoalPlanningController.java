package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.GoalPlanning;
import com.financial.business.entity.conveter.GoalPlanningStructMapper;
import com.financial.business.entity.dto.GoalPlanningDTO;
import com.financial.business.service.IGoalPlanningService;
import com.financial.common.core.domain.R;
import com.financial.common.core.utils.excel.ExcelUtils;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.PageResponse;
import com.financial.common.core.web.page.TableDataInfo;
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
//    @RequiresPermissions("business:goalPlanning:list")
    @PostMapping("/list")
    public PageResponse<GoalPlanningDTO> list(
        @RequestBody GoalPlanningDTO goalPlanningDTO,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<GoalPlanning> page = new Page<>(pageNum, pageSize);
        goalPlanningDTO.setUserId(SecurityUtils.getUserId());
        IPage<GoalPlanningDTO> result = goalPlanningService.list(goalPlanningDTO, page);
        return PageResponse.success((int) result.getCurrent(), (int) result.getSize(), result.getRecords(), result.getTotal());
    }

    @Operation(summary = "导出目标规划列表")
//    @RequiresPermissions("business:goalPlanning:export")
    @Log(title = "目标规划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GoalPlanningDTO goalPlanningDTO)
        throws IOException, UnsupportedEncodingException {
        goalPlanningDTO.setUserId(SecurityUtils.getUserId());
        goalPlanningService.export(goalPlanningDTO, ExcelUtils.setFileName(response, "目标规划"));
    }

    /**
     * 根据目标规划编号获取详细信息
     */
    @Operation(summary = "根据目标规划编号获取详细信息")
//    @RequiresPermissions("business:goalPlanning:query")
    @GetMapping(value = "/{id}")
    public R<GoalPlanningDTO> getInfo(@PathVariable Long id) {
        GoalPlanning goalPlanning = goalPlanningService.getById(id);
        if (goalPlanning == null) {
            return R.fail("目标规划不存在");
        }
        GoalPlanningDTO goalPlanningDTO = goalPlanningStructMapper.toDto(goalPlanning);
        return R.ok(goalPlanningDTO);
    }

    /**
     * 新增目标规划
     */
    @Operation(summary = "新增目标规划")
//    @RequiresPermissions("business:goalPlanning:add")
    @Log(title = "目标规划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody GoalPlanningDTO goalPlanningDTO) {
        goalPlanningDTO.setUserId(SecurityUtils.getUserId());
        goalPlanningDTO.setStatus("进行中");
        goalPlanningDTO.setPriority("中");
        goalPlanningDTO.setCurrentAmount(BigDecimal.ZERO);
        GoalPlanning goalPlanning = goalPlanningStructMapper.toEntity(goalPlanningDTO);
        return toAjax(goalPlanningService.save(goalPlanning));
    }

    /**
     * 修改目标规划
     */
    @Operation(summary = "修改目标规划")
//    @RequiresPermissions("business:goalPlanning:edit")
    @Log(title = "目标规划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody GoalPlanningDTO goalPlanningDTO) {
        goalPlanningDTO.setUserId(SecurityUtils.getUserId());
        GoalPlanning goalPlanning = goalPlanningStructMapper.toEntity(goalPlanningDTO);
        return toAjax(goalPlanningService.updateById(goalPlanning));
    }

    /**
     * 删除目标规划
     */
    @Operation(summary = "删除目标规划")
//    @RequiresPermissions("business:goalPlanning:remove")
    @Log(title = "目标规划", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(goalPlanningService.removeBatchByIds(Arrays.asList(ids)));
    }
}