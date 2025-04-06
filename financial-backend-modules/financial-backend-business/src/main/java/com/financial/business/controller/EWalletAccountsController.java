package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.business.entity.EWalletAccounts;
import com.financial.business.entity.conveter.EWalletAccountsStructMapper;
import com.financial.business.entity.dto.EWalletAccountsDTO;
import com.financial.business.service.IEWalletAccountsService;
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
 * 电子钱包账户表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/eWalletAccounts")
@Tag(name = "电子钱包账户表")
public class EWalletAccountsController extends BaseController {

    @Resource
    private IEWalletAccountsService eWalletAccountsService;

    @Resource
    private EWalletAccountsStructMapper eWalletAccountsStructMapper;

    /**
     * 获取电子钱包账户列表
     */
    @Operation(summary = "获取电子钱包账户列表")
    @RequiresPermissions("business:eWalletAccounts:list")
    @GetMapping("/list")
    public TableDataInfo list(EWalletAccountsDTO eWalletAccountsDTO) {
        startPage();
        EWalletAccounts eWalletAccounts = eWalletAccountsStructMapper.toEntity(eWalletAccountsDTO);
        List<EWalletAccounts> list = eWalletAccountsService.list(new QueryWrapper<>(eWalletAccounts));
        return getDataTable(eWalletAccountsStructMapper.toDtoList(list));
    }

    /**
     * 根据电子钱包账户编号获取详细信息
     */
    @Operation(summary = "根据电子钱包账户编号获取详细信息")
    @RequiresPermissions("business:eWalletAccounts:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(eWalletAccountsService.getById(id));
    }

    /**
     * 新增电子钱包账户
     */
    @Operation(summary = "新增电子钱包账户")
    @RequiresPermissions("business:eWalletAccounts:add")
    @Log(title = "电子钱包账户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody EWalletAccounts eWalletAccounts) {
        return toAjax(eWalletAccountsService.save(eWalletAccounts));
    }

    /**
     * 修改电子钱包账户
     */
    @Operation(summary = "修改电子钱包账户")
    @RequiresPermissions("business:eWalletAccounts:edit")
    @Log(title = "电子钱包账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody EWalletAccounts eWalletAccounts) {
        return toAjax(eWalletAccountsService.updateById(eWalletAccounts));
    }

    /**
     * 删除电子钱包账户
     */
    @Operation(summary = "删除电子钱包账户")
    @RequiresPermissions("business:eWalletAccounts:remove")
    @Log(title = "电子钱包账户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(eWalletAccountsService.removeBatchByIds(Arrays.asList(ids)));
    }
}