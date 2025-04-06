package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.business.entity.BudgetManagement;
import com.financial.business.entity.conveter.BudgetManagementStructMapper;
import com.financial.business.entity.dto.BudgetManagementDTO;
import com.financial.business.service.IBudgetManagementService;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.TableDataInfo;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.security.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 预算管理表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/budgetManagement")
@Tag(name = "预算管理表")
public class BudgetManagementController extends BaseController {

    @Resource
    private IBudgetManagementService budgetManagementService;

    @Resource
    private BudgetManagementStructMapper budgetManagementStructMapper;

    /**
     * 获取预算管理列表
     */
    @Operation(summary = "获取预算管理列表")
//    @RequiresPermissions("business:budgetManagement:list")
    @GetMapping("/list")
    public TableDataInfo list(BudgetManagementDTO budgetManagementDTO) {
        startPage();
        BudgetManagement budgetManagement = budgetManagementStructMapper.toEntity(budgetManagementDTO);
        List<BudgetManagement> list = budgetManagementService.list(new QueryWrapper<>(budgetManagement));
        return getDataTable(budgetManagementStructMapper.toDtoList(list));
    }

    /**
     * 根据预算编号获取详细信息
     */
    @Operation(summary = "根据预算编号获取详细信息")
//    @RequiresPermissions("business:budgetManagement:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        BudgetManagementDTO budgetManagementDTO = budgetManagementStructMapper.toDto(budgetManagementService.getById(id));
        return success(budgetManagementDTO);
    }

    /**
     * 新增预算
     */
    @Operation(summary = "新增预算")
//    @RequiresPermissions("business:budgetManagement:add")
    @Log(title = "预算管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BudgetManagementDTO budgetManagementDTO) {
        BudgetManagement budgetManagement = budgetManagementStructMapper.toEntity(budgetManagementDTO);
        return toAjax(budgetManagementService.save(budgetManagement));
    }

    /**
     * 修改预算
     */
    @Operation(summary = "修改预算")
//    @RequiresPermissions("business:budgetManagement:edit")
    @Log(title = "预算管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BudgetManagementDTO budgetManagementDTO) {
        BudgetManagement budgetManagement = budgetManagementStructMapper.toEntity(budgetManagementDTO);
        return toAjax(budgetManagementService.updateById(budgetManagement));
    }

    /**
     * 删除预算
     */
    @Operation(summary = "删除预算")
//    @RequiresPermissions("business:budgetManagement:remove")
    @Log(title = "预算管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(budgetManagementService.removeBatchByIds(Arrays.asList(ids)));
    }
}