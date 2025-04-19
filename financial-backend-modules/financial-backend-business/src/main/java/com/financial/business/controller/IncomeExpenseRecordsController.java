package com.financial.business.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.IncomeExpenseRecords;
import com.financial.business.entity.conveter.IncomeExpenseRecordsStructMapper;
import com.financial.business.entity.dto.IncomeExpenseRecordsDTO;
import com.financial.business.service.IIncomeExpenseRecordsService;
import com.financial.common.core.utils.excel.ExcelUtils;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.PageResponse;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.security.utils.SecurityUtils;
import com.itextpdf.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 收支记录表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/incomeExpenseRecords")
@Tag(name = "收支记录表")
public class IncomeExpenseRecordsController extends BaseController {

    @Resource
    private IIncomeExpenseRecordsService incomeExpenseRecordsService;

    @Resource
    private IncomeExpenseRecordsStructMapper incomeExpenseRecordsStructMapper;

    @Operation(summary = "收支记录表列表")
//    @RequiresPermissions("business:incomeExpenseRecords:list")
    @PostMapping("/list")
    public PageResponse<IncomeExpenseRecordsDTO> list(
        @RequestBody IncomeExpenseRecordsDTO incomeExpenseRecordsDTO,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        //这里要获取相关用户的数据
        incomeExpenseRecordsDTO.setUserId(SecurityUtils.getUserId());
        Page<IncomeExpenseRecords> page = new Page<>(pageNum, pageSize);
        IPage<IncomeExpenseRecordsDTO> result = incomeExpenseRecordsService.list(incomeExpenseRecordsDTO, page);
        return PageResponse.success((int) result.getCurrent(), (int) result.getSize(), result.getRecords(), result.getTotal()
        );
    }

    /**
     * 根据收支记录编号获取详细信息
     */
    @Operation(summary = "根据收支记录编号获取详细信息")
//    @RequiresPermissions("business:incomeExpenseRecords:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        IncomeExpenseRecords record = incomeExpenseRecordsService.getById(id);
        if(record==null){
            return error("该记录不存在");
        }
        return success(incomeExpenseRecordsStructMapper.toDto(record));
    }

    /**
     * 新增收支记录
     */
    @Operation(summary = "新增收支记录")
//    @RequiresPermissions("business:incomeExpenseRecords:add")
    @Log(title = "收支记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody IncomeExpenseRecordsDTO incomeExpenseRecordsDTO) {
        //dto转化为实体类
        incomeExpenseRecordsDTO.setUserId(SecurityUtils.getUserId());
        IncomeExpenseRecords incomeExpenseRecords = incomeExpenseRecordsStructMapper.toEntity(incomeExpenseRecordsDTO);
        return toAjax(incomeExpenseRecordsService.save(incomeExpenseRecords));
    }

    /**
     * 修改收支记录
     */
    @Operation(summary = "修改收支记录")
//    @RequiresPermissions("business:incomeExpenseRecords:edit")
    @Log(title = "收支记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody IncomeExpenseRecordsDTO incomeExpenseRecordsDTO) {
        incomeExpenseRecordsDTO.setUserId(SecurityUtils.getUserId());
        IncomeExpenseRecords incomeExpenseRecords = incomeExpenseRecordsStructMapper.toEntity(incomeExpenseRecordsDTO);
        return toAjax(incomeExpenseRecordsService.updateById(incomeExpenseRecords));
    }

    /**
     * 删除收支记录
     */
    @Operation(summary = "删除收支记录")
//    @RequiresPermissions("business:incomeExpenseRecords:remove")
    @Log(title = "收支记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(incomeExpenseRecordsService.removeBatchByIds(Arrays.asList(ids)));
    }

    /**
     * 导出收支记录列表,可以选几条数据，也可以全部导出
     */
    @Operation(summary = "导出收支记录列表")
//    @RequiresPermissions("business:incomeExpenseRecords:export")
    @Log(title = "收支记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, IncomeExpenseRecordsDTO incomeExpenseRecordsDTO)
        throws IOException, UnsupportedEncodingException {
        //获取数据
        incomeExpenseRecordsDTO.setUserId(SecurityUtils.getUserId());
        incomeExpenseRecordsService.export(incomeExpenseRecordsDTO, ExcelUtils.setFileName(response, "收支记录"));
    }
}