package com.financial.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.RestController;
import com.financial.common.core.utils.poi.ExcelUtil;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.TableDataInfo;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.security.annotation.RequiresPermissions;
import com.financial.common.security.utils.SecurityUtils;
import com.financial.system.api.domain.SysDictType;
import com.financial.system.service.ISysDictTypeService;

/**
 * 数据字典信息
 * 
 * @author xinyi
 */
@Tag(name = "字典类型")
@RestController
@RequestMapping("/dict/type")
public class SysDictTypeController extends BaseController {
    @Resource
    private ISysDictTypeService dictTypeService;

    @Operation(summary = "字典类型列表")
    @RequiresPermissions("system:dict:list")
    @GetMapping("/list")
    public TableDataInfo list(SysDictType dictType) {
        startPage();
        List<SysDictType> list = dictTypeService.list(new QueryWrapper<SysDictType>(dictType));
        return getDataTable(list);
    }

    @Operation(summary = "字典类型导出")
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:dict:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDictType dictType) {
        List<SysDictType> list = dictTypeService.list(new QueryWrapper<SysDictType>(dictType));
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        util.exportExcel(response, list, "字典类型");
    }

    /**
     * 查询字典类型详细
     */
    @Operation(summary = "字典类型详情")
    @RequiresPermissions("system:dict:query")
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable Long dictId) {
        return success(dictTypeService.getById(dictId));
    }

    /**
     * 新增字典类型
     */
    @Operation(summary = "新增字典类型")
//    @RequiresPermissions("system:dict:add")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict))
        {
            return error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictTypeService.save(dict));
    }

    /**
     * 修改字典类型
     */
    @Operation(summary = "修改字典类型")
//    @RequiresPermissions("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictType dict) {
        if (!dictTypeService.checkDictTypeUnique(dict))
        {
            return error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictTypeService.updateById(dict));
    }

    /**
     * 删除字典类型
     */
    @Operation(summary = "删除字典类型")
    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public AjaxResult remove(@PathVariable Long[] dictIds) {
        dictTypeService.removeByIds(Arrays.asList(dictIds));
        return success();
    }

    /**
     * 刷新字典缓存
     */
    @Operation(summary = "刷新字典缓存")
    @RequiresPermissions("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache() {
        dictTypeService.resetDictCache();
        return success();
    }

    /**
     * 获取字典选择框列表
     */
    @Operation(summary = "获取字典选择框列表")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<SysDictType> dictTypes = dictTypeService.list();
        return success(dictTypes);
    }
}
