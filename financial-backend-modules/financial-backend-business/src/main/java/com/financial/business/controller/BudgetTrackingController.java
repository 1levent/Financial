package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.BudgetTracking;
import com.financial.business.entity.conveter.BudgetTrackingStructMapper;
import com.financial.business.entity.dto.BudgetTrackingDTO;
import com.financial.business.service.IBudgetTrackingService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/business/budgetTracking")
@Tag(name = "预算跟踪表")
public class BudgetTrackingController extends BaseController {

    @Resource
    private IBudgetTrackingService budgetTrackingService;

    @Resource
    private BudgetTrackingStructMapper budgetTrackingStructMapper;

    @Operation(summary = "获取预算跟踪列表")
//    @RequiresPermissions("business:budgetTracking:list")
    @PostMapping("/list")
    public PageResponse<BudgetTrackingDTO> list(
        @RequestBody BudgetTrackingDTO budgetTrackingDTO,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<BudgetTracking> page = new Page<>(pageNum, pageSize);
        budgetTrackingDTO.setUserId(SecurityUtils.getUserId());
        IPage<BudgetTrackingDTO> result = budgetTrackingService.list(budgetTrackingDTO, page);
        result.getRecords().forEach(item -> {
            budgetTrackingService.getInfoByBudgetId(item.getBudgetId(),item);
        });
        return PageResponse.success((int) result.getCurrent(), (int) result.getSize(), result.getRecords(), result.getTotal());
    }


    @Operation(summary = "根据预算跟踪编号获取详细信息")
//    @RequiresPermissions("business:budgetTracking:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(budgetTrackingService.getById(id));
    }

    @Operation(summary = "新增预算跟踪")
//    @RequiresPermissions("business:budgetTracking:add")
    @Log(title = "预算跟踪", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BudgetTrackingDTO budgetTrackingDTO) {
        budgetTrackingDTO.setUserId(SecurityUtils.getUserId());
        BudgetTracking budgetTracking = budgetTrackingStructMapper.toEntity(budgetTrackingDTO);
        return toAjax(budgetTrackingService.save(budgetTracking));
    }

    @Operation(summary = "修改预算跟踪")
//    @RequiresPermissions("business:budgetTracking:edit")
    @Log(title = "预算跟踪", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BudgetTrackingDTO budgetTrackingDTO) {
        budgetTrackingDTO.setUserId(SecurityUtils.getUserId());
        BudgetTracking budgetTracking = budgetTrackingStructMapper.toEntity(budgetTrackingDTO);
        return toAjax(budgetTrackingService.updateById(budgetTracking));
    }

    @Operation(summary = "删除预算跟踪")
//    @RequiresPermissions("business:budgetTracking:remove")
    @Log(title = "预算跟踪", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(budgetTrackingService.removeBatchByIds(Arrays.asList(ids)));
    }
}