package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.business.entity.TransactionRecords;
import com.financial.business.entity.conveter.TransactionRecordsStructMapper;
import com.financial.business.entity.dto.TransactionRecordsDTO;
import com.financial.business.service.ITransactionRecordsService;
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
 * 交易记录表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/transactionRecords")
@Tag(name = "交易记录表")
public class TransactionRecordsController extends BaseController {

    @Resource
    private ITransactionRecordsService transactionRecordsService;

    @Resource
    private TransactionRecordsStructMapper transactionRecordsStructMapper;

    /**
     * 获取交易记录列表
     */
    @Operation(summary = "获取交易记录列表")
    @RequiresPermissions("business:transactionRecords:list")
    @GetMapping("/list")
    public TableDataInfo list(TransactionRecordsDTO transactionRecordsDTO) {
        startPage();
        TransactionRecords transactionRecords = transactionRecordsStructMapper.toEntity(transactionRecordsDTO);
        List<TransactionRecords> list = transactionRecordsService.list(new QueryWrapper<>(transactionRecords));
        return getDataTable(transactionRecordsStructMapper.toDtoList(list));
    }

    /**
     * 根据交易记录编号获取详细信息
     */
    @Operation(summary = "根据交易记录编号获取详细信息")
    @RequiresPermissions("business:transactionRecords:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(transactionRecordsService.getById(id));
    }

    /**
     * 新增交易记录
     */
    @Operation(summary = "新增交易记录")
    @RequiresPermissions("business:transactionRecords:add")
    @Log(title = "交易记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TransactionRecords transactionRecords) {
        return toAjax(transactionRecordsService.save(transactionRecords));
    }

    /**
     * 修改交易记录
     */
    @Operation(summary = "修改交易记录")
    @RequiresPermissions("business:transactionRecords:edit")
    @Log(title = "交易记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TransactionRecords transactionRecords) {
        return toAjax(transactionRecordsService.updateById(transactionRecords));
    }

    /**
     * 删除交易记录
     */
    @Operation(summary = "删除交易记录")
    @RequiresPermissions("business:transactionRecords:remove")
    @Log(title = "交易记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(transactionRecordsService.removeBatchByIds(Arrays.asList(ids)));
    }
}