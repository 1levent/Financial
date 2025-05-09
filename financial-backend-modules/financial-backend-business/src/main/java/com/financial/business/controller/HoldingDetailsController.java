package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.business.entity.HoldingDetails;
import com.financial.business.entity.conveter.HoldingDetailsStructMapper;
import com.financial.business.entity.dto.HoldingDetailsDTO;
import com.financial.business.service.IHoldingDetailsService;
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
 * 持仓明细表 前端控制器
 * </p>
 *
 * @author xinyi
 * @since 2025-03-28
 */
@RestController
@RequestMapping("/business/holdingDetails")
@Tag(name = "持仓明细表")
public class HoldingDetailsController extends BaseController {

    @Resource
    private IHoldingDetailsService holdingDetailsService;

    @Resource
    private HoldingDetailsStructMapper holdingDetailsStructMapper;

    /**
     * 获取持仓明细列表
     */
    @Operation(summary = "获取持仓明细列表")
    @RequiresPermissions("business:holdingDetails:list")
    @GetMapping("/list")
    public TableDataInfo list(HoldingDetailsDTO holdingDetailsDTO) {
        startPage();
        HoldingDetails holdingDetails = holdingDetailsStructMapper.toEntity(holdingDetailsDTO);
        List<HoldingDetails> list = holdingDetailsService.list(new QueryWrapper<>(holdingDetails));
        return getDataTable(holdingDetailsStructMapper.toDtoList(list));
    }

    /**
     * 根据持仓明细编号获取详细信息
     */
    @Operation(summary = "根据持仓明细编号获取详细信息")
    @RequiresPermissions("business:holdingDetails:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(holdingDetailsService.getById(id));
    }

    /**
     * 新增持仓明细
     */
    @Operation(summary = "新增持仓明细")
    @RequiresPermissions("business:holdingDetails:add")
    @Log(title = "持仓明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HoldingDetails holdingDetails) {
        return toAjax(holdingDetailsService.save(holdingDetails));
    }

    /**
     * 修改持仓明细
     */
    @Operation(summary = "修改持仓明细")
    @RequiresPermissions("business:holdingDetails:edit")
    @Log(title = "持仓明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody HoldingDetails holdingDetails) {
        return toAjax(holdingDetailsService.updateById(holdingDetails));
    }

    /**
     * 删除持仓明细
     */
    @Operation(summary = "删除持仓明细")
    @RequiresPermissions("business:holdingDetails:remove")
    @Log(title = "持仓明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(holdingDetailsService.removeBatchByIds(Arrays.asList(ids)));
    }
}