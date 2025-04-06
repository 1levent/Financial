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
import com.financial.common.core.constant.CacheConstants;
import com.financial.common.core.utils.poi.ExcelUtil;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.TableDataInfo;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.redis.service.RedisService;
import com.financial.common.security.annotation.InnerAuth;
import com.financial.common.security.annotation.RequiresPermissions;
import com.financial.system.api.domain.SysLogininfor;
import com.financial.system.service.ISysLogininforService;

/**
 * 系统访问记录
 * 
 * @author xinyi
 */
@Tag(name = "系统访问日志")
@RestController
@RequestMapping("/logininfor")
public class SysLogininforController extends BaseController {
    @Resource
    private ISysLogininforService logininforService;

    @Resource
    private RedisService redisService;

    @Operation(summary = "登录日志列表")
    @RequiresPermissions("system:logininfor:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLogininfor logininfor) {
        startPage();
        List<SysLogininfor> list = logininforService.list(new QueryWrapper<>(logininfor));
        return getDataTable(list);
    }

    @Operation(summary = "登录日志导出")
    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:logininfor:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLogininfor logininfor) {
        List<SysLogininfor> list = logininforService.list(new QueryWrapper<>(logininfor));
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        util.exportExcel(response, list, "登录日志");
    }

    @Operation(summary = "登录日志删除")
    @RequiresPermissions("system:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.removeBatchByIds(Arrays.asList(infoIds)));
    }

    @Operation(summary = "登录日志清空")
    @RequiresPermissions("system:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        logininforService.remove(new QueryWrapper<>());
        return success();
    }

    @Operation(summary = "账户解锁")
    @RequiresPermissions("system:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public AjaxResult unlock(@PathVariable("userName") String userName) {
        redisService.deleteObject(CacheConstants.PWD_ERR_CNT_KEY + userName);
        return success();
    }

    @Operation(summary = "登录日志新增")
    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysLogininfor logininfor) {
        return toAjax(logininforService.save(logininfor));
    }
}
