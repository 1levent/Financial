package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.business.entity.CreditCardAccounts;
import com.financial.business.entity.conveter.CreditCardAccountsStructMapper;
import com.financial.business.entity.dto.CreditCardAccountsDTO;
import com.financial.business.service.ICreditCardAccountsService;
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

@RestController
@RequestMapping("/business/creditCardAccounts")
@Tag(name = "信用卡账户表")
public class CreditCardAccountsController extends BaseController {

    @Resource
    private ICreditCardAccountsService creditCardAccountsService;

    @Resource
    private CreditCardAccountsStructMapper creditCardAccountsStructMapper;

    @Operation(summary = "获取信用卡账户列表")
    @RequiresPermissions("business:creditCardAccounts:list")
    @GetMapping("/list")
    public TableDataInfo list(CreditCardAccountsDTO creditCardAccountsDTO) {
        startPage();
        CreditCardAccounts creditCardAccounts = creditCardAccountsStructMapper.toEntity(creditCardAccountsDTO);
        List<CreditCardAccounts> list = creditCardAccountsService.list(new QueryWrapper<>(creditCardAccounts));
        return getDataTable(creditCardAccountsStructMapper.toDtoList(list));
    }

    @Operation(summary = "根据信用卡账户编号获取详细信息")
    @RequiresPermissions("business:creditCardAccounts:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(creditCardAccountsService.getById(id));
    }

    @Operation(summary = "新增信用卡账户")
    @RequiresPermissions("business:creditCardAccounts:add")
    @Log(title = "信用卡账户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody CreditCardAccounts creditCardAccounts) {
        return toAjax(creditCardAccountsService.save(creditCardAccounts));
    }

    @Operation(summary = "修改信用卡账户")
    @RequiresPermissions("business:creditCardAccounts:edit")
    @Log(title = "信用卡账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody CreditCardAccounts creditCardAccounts) {
        return toAjax(creditCardAccountsService.updateById(creditCardAccounts));
    }

    @Operation(summary = "删除信用卡账户")
    @RequiresPermissions("business:creditCardAccounts:remove")
    @Log(title = "信用卡账户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(creditCardAccountsService.removeBatchByIds(Arrays.asList(ids)));
    }
}