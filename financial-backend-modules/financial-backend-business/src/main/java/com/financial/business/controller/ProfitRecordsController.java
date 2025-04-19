package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.ProfitRecords;
import com.financial.business.entity.conveter.ProfitRecordsStructMapper;
import com.financial.business.entity.dto.ProfitRecordsDTO;
import com.financial.business.service.IProfitRecordsService;
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
 * 收益记录表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/profitRecords")
@Tag(name = "收益记录表")
public class ProfitRecordsController extends BaseController {

    @Resource
    private IProfitRecordsService profitRecordsService;

    @Resource
    private ProfitRecordsStructMapper profitRecordsStructMapper;

    /**
     * 获取收益记录列表
     */
    @Operation(summary = "获取收益记录列表")
//    @RequiresPermissions("business:profitRecords:list")
    @PostMapping("/list")
    public PageResponse<ProfitRecordsDTO> list(
        @RequestBody ProfitRecordsDTO profitRecordsDTO,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<ProfitRecords> page = new Page<>(pageNum, pageSize);
        profitRecordsDTO.setUserId(SecurityUtils.getUserId());
        IPage<ProfitRecordsDTO> result = profitRecordsService.list(profitRecordsDTO, page);
        return PageResponse.success((int) result.getCurrent(), (int) result.getSize(), result.getRecords(), result.getTotal());
    }

    @Operation(summary = "导出收益记录列表")
//    @RequiresPermissions("business:profitRecords:export")
    @Log(title = "收益记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProfitRecordsDTO profitRecordsDTO)
        throws IOException, UnsupportedEncodingException {
        profitRecordsDTO.setUserId(SecurityUtils.getUserId());
        profitRecordsService.export(profitRecordsDTO, ExcelUtils.setFileName(response, "收益记录"));
    }

    /**
     * 根据收益记录编号获取详细信息
     */
    @Operation(summary = "根据收益记录编号获取详细信息")
//    @RequiresPermissions("business:profitRecords:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(profitRecordsService.getById(id));
    }

    /**
     * 新增收益记录
     */
    @Operation(summary = "新增收益记录")
//    @RequiresPermissions("business:profitRecords:add")
    @Log(title = "收益记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody ProfitRecordsDTO profitRecordsDTO) {
        profitRecordsDTO.setUserId(SecurityUtils.getUserId());
        ProfitRecords profitRecords = profitRecordsStructMapper.toEntity(profitRecordsDTO);
        return toAjax(profitRecordsService.save(profitRecords));
    }

    /**
     * 修改收益记录
     */
    @Operation(summary = "修改收益记录")
//    @RequiresPermissions("business:profitRecords:edit")
    @Log(title = "收益记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody ProfitRecordsDTO profitRecordsDTO) {
        profitRecordsDTO.setUserId(SecurityUtils.getUserId());
        ProfitRecords profitRecords = profitRecordsStructMapper.toEntity(profitRecordsDTO);
        return toAjax(profitRecordsService.updateById(profitRecords));
    }

    /**
     * 删除收益记录
     */
    @Operation(summary = "删除收益记录")
//    @RequiresPermissions("business:profitRecords:remove")
    @Log(title = "收益记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(profitRecordsService.removeBatchByIds(Arrays.asList(ids)));
    }
}