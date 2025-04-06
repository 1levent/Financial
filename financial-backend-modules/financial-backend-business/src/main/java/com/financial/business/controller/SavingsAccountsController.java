package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.business.entity.SavingsAccounts;
import com.financial.business.entity.conveter.SavingsAccountsStructMapper;
import com.financial.business.entity.dto.SavingsAccountsDTO;
import com.financial.business.service.ISavingsAccountsService;
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
 * 储蓄卡账户表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/savingsAccounts")
@Tag(name = "储蓄卡账户表")
public class SavingsAccountsController extends BaseController {

    @Resource
    private ISavingsAccountsService savingsAccountsService;

    @Resource
    private SavingsAccountsStructMapper savingsAccountsStructMapper;

    /**
     * 获取储蓄卡账户列表
     */
    @Operation(summary = "获取储蓄卡账户列表")
    @RequiresPermissions("business:savingsAccounts:list")
    @GetMapping("/list")
    public TableDataInfo list(SavingsAccountsDTO savingsAccountsDTO) {
        startPage();
        SavingsAccounts savingsAccounts = savingsAccountsStructMapper.toEntity(savingsAccountsDTO);
        List<SavingsAccounts> list = savingsAccountsService.list(new QueryWrapper<>(savingsAccounts));
        return getDataTable(savingsAccountsStructMapper.toDtoList(list));
    }

    /**
     * 根据储蓄卡账户编号获取详细信息
     */
    @Operation(summary = "根据储蓄卡账户编号获取详细信息")
    @RequiresPermissions("business:savingsAccounts:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(savingsAccountsService.getById(id));
    }

    /**
     * 新增储蓄卡账户
     */
    @Operation(summary = "新增储蓄卡账户")
    @RequiresPermissions("business:savingsAccounts:add")
    @Log(title = "储蓄卡账户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SavingsAccounts savingsAccounts) {
        return toAjax(savingsAccountsService.save(savingsAccounts));
    }

    /**
     * 修改储蓄卡账户
     */
    @Operation(summary = "修改储蓄卡账户")
    @RequiresPermissions("business:savingsAccounts:edit")
    @Log(title = "储蓄卡账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SavingsAccounts savingsAccounts) {
        return toAjax(savingsAccountsService.updateById(savingsAccounts));
    }

    /**
     * 删除储蓄卡账户
     */
    @Operation(summary = "删除储蓄卡账户")
    @RequiresPermissions("business:savingsAccounts:remove")
    @Log(title = "储蓄卡账户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(savingsAccountsService.removeBatchByIds(Arrays.asList(ids)));
    }
}