package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.TransactionRecords;
import com.financial.business.entity.conveter.TransactionRecordsStructMapper;
import com.financial.business.entity.dto.TransactionRecordsDTO;
import com.financial.business.service.ITransactionRecordsService;
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
//    @RequiresPermissions("business:transactionRecords:list")
    @PostMapping("/list")
    public PageResponse<TransactionRecordsDTO> list(
        @RequestBody TransactionRecordsDTO transactionRecordsDTO,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<TransactionRecords> page = new Page<>(pageNum, pageSize);
        transactionRecordsDTO.setUserId(SecurityUtils.getUserId());
        IPage<TransactionRecordsDTO> result = transactionRecordsService.list(transactionRecordsDTO, page);
        return PageResponse.success((int) result.getCurrent(), (int) result.getSize(), result.getRecords(), result.getTotal());
    }

    @Operation(summary = "导出交易记录列表")
//    @RequiresPermissions("business:transactionRecords:export")
    @Log(title = "交易记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TransactionRecordsDTO transactionRecordsDTO)
        throws IOException, UnsupportedEncodingException {
        transactionRecordsDTO.setUserId(SecurityUtils.getUserId());
        transactionRecordsService.export(transactionRecordsDTO, ExcelUtils.setFileName(response, "交易记录"));
    }

    /**
     * 根据交易记录编号获取详细信息
     */
    @Operation(summary = "根据交易记录编号获取详细信息")
//    @RequiresPermissions("business:transactionRecords:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(transactionRecordsService.getById(id));
    }

    /**
     * 新增交易记录
     */
    @Operation(summary = "新增交易记录")
//    @RequiresPermissions("business:transactionRecords:add")
    @Log(title = "交易记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody TransactionRecordsDTO transactionRecordsDTO) {
        transactionRecordsDTO.setUserId(SecurityUtils.getUserId());
        TransactionRecords transactionRecords = transactionRecordsStructMapper.toEntity(transactionRecordsDTO);
        return toAjax(transactionRecordsService.save(transactionRecords));
    }

    /**
     * 修改交易记录
     */
    @Operation(summary = "修改交易记录")
//    @RequiresPermissions("business:transactionRecords:edit")
    @Log(title = "交易记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TransactionRecordsDTO transactionRecordsDTO) {
        transactionRecordsDTO.setUserId(SecurityUtils.getUserId());
        TransactionRecords transactionRecords = transactionRecordsStructMapper.toEntity(transactionRecordsDTO);
        return toAjax(transactionRecordsService.updateById(transactionRecords));
    }

    /**
     * 删除交易记录
     */
    @Operation(summary = "删除交易记录")
//    @RequiresPermissions("business:transactionRecords:remove")
    @Log(title = "交易记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(transactionRecordsService.removeBatchByIds(Arrays.asList(ids)));
    }
}