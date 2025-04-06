package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.business.entity.LoanRecords;
import com.financial.business.entity.conveter.LoanRecordsStructMapper;
import com.financial.business.entity.dto.LoanRecordsDTO;
import com.financial.business.service.ILoanRecordsService;
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
 * 借贷记录表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/loanRecords")
@Tag(name = "借贷记录表")
public class LoanRecordsController extends BaseController {

    @Resource
    private ILoanRecordsService loanRecordsService;

    @Resource
    private LoanRecordsStructMapper loanRecordsStructMapper;

    /**
     * 获取借贷记录列表
     */
    @Operation(summary = "获取借贷记录列表")
    @RequiresPermissions("business:loanRecords:list")
    @GetMapping("/list")
    public TableDataInfo list(LoanRecordsDTO loanRecordsDTO) {
        startPage();
        LoanRecords loanRecords = loanRecordsStructMapper.toEntity(loanRecordsDTO);
        List<LoanRecords> list = loanRecordsService.list(new QueryWrapper<>(loanRecords));
        return getDataTable(loanRecordsStructMapper.toDtoList(list));
    }

    /**
     * 根据借贷记录编号获取详细信息
     */
    @Operation(summary = "根据借贷记录编号获取详细信息")
    @RequiresPermissions("business:loanRecords:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(loanRecordsService.getById(id));
    }

    /**
     * 新增借贷记录
     */
    @Operation(summary = "新增借贷记录")
    @RequiresPermissions("business:loanRecords:add")
    @Log(title = "借贷记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody LoanRecords loanRecords) {
        return toAjax(loanRecordsService.save(loanRecords));
    }

    /**
     * 修改借贷记录
     */
    @Operation(summary = "修改借贷记录")
    @RequiresPermissions("business:loanRecords:edit")
    @Log(title = "借贷记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody LoanRecords loanRecords) {
        return toAjax(loanRecordsService.updateById(loanRecords));
    }

    /**
     * 删除借贷记录
     */
    @Operation(summary = "删除借贷记录")
    @RequiresPermissions("business:loanRecords:remove")
    @Log(title = "借贷记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(loanRecordsService.removeBatchByIds(Arrays.asList(ids)));
    }
}