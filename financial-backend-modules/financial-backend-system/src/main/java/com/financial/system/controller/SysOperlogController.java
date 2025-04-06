package com.financial.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financial.common.core.utils.poi.ExcelUtil;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.TableDataInfo;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.security.annotation.InnerAuth;
import com.financial.common.security.annotation.RequiresPermissions;
import com.financial.system.api.domain.SysOperLog;
import com.financial.system.service.ISysOperLogService;

/**
 * 操作日志记录
 * 
 * @author xinyi
 */
@Tag(name = "操作日志记录")
@RestController
@RequestMapping("/operlog")
public class SysOperlogController extends BaseController {
    @Resource
    private ISysOperLogService operLogService;

    @Operation(summary = "操作日志列表")
    @RequiresPermissions("system:operlog:list")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = operLogService.list(new QueryWrapper<>(operLog));
        return getDataTable(list);
    }

    @Operation(summary = "操作日志导出")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:operlog:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLog operLog) {
        List<SysOperLog> list = operLogService.list(new QueryWrapper<>(operLog));
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }

    @Operation(summary = "操作日志删除")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:operlog:remove")
    @DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.removeByIds(Arrays.asList(operIds)));
    }

    @Operation(summary = "操作日志清空")
    @RequiresPermissions("system:operlog:remove")
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        operLogService.remove(new QueryWrapper<>());
        return success();
    }

    @Operation(summary = "操作日志新增")
    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysOperLog operLog) {
        return toAjax(operLogService.save(operLog));
    }
}
