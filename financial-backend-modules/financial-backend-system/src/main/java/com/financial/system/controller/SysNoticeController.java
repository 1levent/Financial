package com.financial.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
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
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.TableDataInfo;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.security.annotation.RequiresPermissions;
import com.financial.common.security.utils.SecurityUtils;
import com.financial.system.domain.SysNotice;
import com.financial.system.service.ISysNoticeService;

/**
 * 公告 信息操作处理
 * 
 * @author xinyi
 */
@Tag(name = "通知公告")
@RestController
@RequestMapping("/notice")
public class SysNoticeController extends BaseController {
    @Resource
    private ISysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @Operation(summary = "通知公告列表")
    @RequiresPermissions("system:notice:list")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.list(new QueryWrapper<>(notice));
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @Operation(summary = "根据通知公告编号获取详细信息")
    @RequiresPermissions("system:notice:query")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId) {
        return success(noticeService.getById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @Operation(summary = "新增通知公告")
    @RequiresPermissions("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice) {
        notice.setCreateBy(SecurityUtils.getUsername());
        return toAjax(noticeService.save(notice));
    }

    /**
     * 修改通知公告
     */
    @Operation(summary = "修改通知公告")
    @RequiresPermissions("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice) {
        notice.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(noticeService.updateById(notice));
    }

    /**
     * 删除通知公告
     */
    @Operation(summary = "删除通知公告")
    @RequiresPermissions("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.removeByIds(Arrays.asList(noticeIds)));
    }
}
