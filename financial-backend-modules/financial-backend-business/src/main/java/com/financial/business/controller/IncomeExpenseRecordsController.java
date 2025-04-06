package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.IncomeExpenseRecords;
import com.financial.business.entity.conveter.IncomeExpenseRecordsStructMapper;
import com.financial.business.entity.dto.IncomeExpenseRecordsDTO;
import com.financial.business.service.IIncomeExpenseRecordsService;
import com.financial.common.core.utils.SpringUtils;
import com.financial.common.core.utils.StringUtils;
import com.financial.common.core.utils.poi.ExcelUtil;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.PageResponse;
import com.financial.common.core.web.page.TableDataInfo;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.security.annotation.RequiresPermissions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
    @GetMapping("/list")
    public PageResponse<IncomeExpenseRecordsDTO> list(
        IncomeExpenseRecordsDTO incomeExpenseRecordsDTO,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
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
        IncomeExpenseRecords incomeExpenseRecords = incomeExpenseRecordsStructMapper.toEntity(incomeExpenseRecordsDTO);
        //设置当前用户的userId，todo 现在默认为1，后续再调试
        incomeExpenseRecords.setUserId(1L);
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

}