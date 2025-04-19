package com.financial.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.common.core.domain.R;
import com.financial.common.core.web.page.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.financial.common.core.utils.StringUtils;

import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.security.utils.SecurityUtils;
import com.financial.system.api.domain.SysDictData;
import com.financial.system.service.ISysDictDataService;
import com.financial.system.service.ISysDictTypeService;

/**
 * 数据字典信息
 * 
 * @author xinyi
 */
@Tag(name = "字典数据")
@RestController
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController {
    @Resource
    private ISysDictDataService dictDataService;
    
    @Resource
    private ISysDictTypeService dictTypeService;

    @Operation(summary = "字典数据列表")
//    @RequiresPermissions("system:dict:list")
    @PostMapping("/list")
    public PageResponse<SysDictData> list(
        @RequestBody SysDictData dictData,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<SysDictData> page = new Page<>(pageNum, pageSize);
        System.out.println("获取字典列表");
        System.out.println("查询条件："+dictData);
        IPage<SysDictData> result = dictDataService.page(page, new QueryWrapper<>(dictData));
        return PageResponse.success((int) result.getCurrent(), (int) result.getSize(), result.getRecords(), result.getTotal());
    }

//    @Operation(summary = "字典数据导出")
//    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
//    @RequiresPermissions("system:dict:export")
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SysDictData dictData) {
//        List<SysDictData> list = dictDataService.list(new QueryWrapper<SysDictData>(dictData));
//        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
//        util.exportExcel(response, list, "字典数据");
//    }

    /**
     * 查询字典数据详细
     */
    @Operation(summary = "字典数据详情")
//    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return success(dictDataService.getById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @Operation(summary = "根据字典类型查询字典数据信息")
    @GetMapping(value = "/type/{dictType}")
    public R<List<SysDictData>> dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return R.ok(data);
    }

    /**
     * 新增字典类型
     */
    @Operation(summary = "新增字典数据")
//    @RequiresPermissions("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.addDict(dict));
    }

    /**
     * 修改保存字典类型
     */
    @Operation(summary = "修改保存字典数据")
//    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.updateById(dict));
    }

    /**
     * 删除字典类型
     */
    @Operation(summary = "删除字典数据")
//    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        dictDataService.removeByIds(Arrays.asList(dictCodes));
        return success();
    }

}
