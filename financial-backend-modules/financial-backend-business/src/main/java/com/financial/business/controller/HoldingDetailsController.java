package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.Account;
import com.financial.business.entity.HoldingDetails;
import com.financial.business.entity.conveter.HoldingDetailsStructMapper;
import com.financial.business.entity.dto.HoldingDetailsDTO;
import com.financial.business.entity.dto.statistic.ProductBarDTO;
import com.financial.business.entity.dto.statistic.ProductPieDTO;
import com.financial.business.service.IAccountService;
import com.financial.business.service.IHoldingDetailsService;
import com.financial.common.core.domain.R;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    @Resource
    private IAccountService  accountService;

    /**
     * 获取持仓明细列表
     */
    @Operation(summary = "获取持仓明细列表")
//    @RequiresPermissions("business:holdingDetails:list")
    @PostMapping("/list")
    public PageResponse<HoldingDetailsDTO> list(
        @RequestBody HoldingDetailsDTO holdingDetailsDTO,
        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        Page<HoldingDetails> page = new Page<>(pageNum, pageSize);
        holdingDetailsDTO.setUserId(SecurityUtils.getUserId());
        IPage<HoldingDetailsDTO> result = holdingDetailsService.list(holdingDetailsDTO, page);
        return PageResponse.success((int) result.getCurrent(), (int) result.getSize(), result.getRecords(), result.getTotal());
    }

    @Operation(summary = "导出持仓明细列表")
//    @RequiresPermissions("business:holdingDetails:export")
    @Log(title = "持仓明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HoldingDetailsDTO holdingDetailsDTO)
        throws IOException, UnsupportedEncodingException {
        holdingDetailsDTO.setUserId(SecurityUtils.getUserId());
        holdingDetailsService.export(holdingDetailsDTO, ExcelUtils.setFileName(response, "持仓明细"));
    }

    /**
     * 根据持仓明细编号获取详细信息
     */
    @Operation(summary = "根据持仓明细编号获取详细信息")
//    @RequiresPermissions("business:holdingDetails:query")
    @GetMapping(value = "/{id}")
    public R<HoldingDetailsDTO> getInfo(@PathVariable Long id) {
        HoldingDetails details = holdingDetailsService.getById(id);
        if (details == null) {
            return R.fail("该记录不存在");
        }
        HoldingDetailsDTO holdingDetailsDTO = holdingDetailsStructMapper.toDto(details);
        return R.ok(holdingDetailsDTO);
    }

    /**
     * 新增持仓明细
     */
    @Operation(summary = "新增持仓明细")
//    @RequiresPermissions("business:holdingDetails:add")
    @Log(title = "持仓明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HoldingDetailsDTO holdingDetailsDTO) {
        holdingDetailsDTO.setUserId(SecurityUtils.getUserId());
        //todo 初始的成本就是持仓金额,不够后续卖出，成本可能会变化
        holdingDetailsDTO.setCost(holdingDetailsDTO.getQuantity());
        holdingDetailsDTO.setProfitLoss(BigDecimal.ZERO);
        holdingDetailsDTO.setReturnRate(BigDecimal.ZERO);
//        holdingDetailsDTO.setStartDate();
        boolean flag = accountService.adjustAmount(holdingDetailsDTO.getAccountNo(), "-",
            holdingDetailsDTO.getCost());
        if (!flag) {
            return error("账户余额不足");
        }
        HoldingDetails holdingDetails = holdingDetailsStructMapper.toEntity(holdingDetailsDTO);
        return toAjax(holdingDetailsService.save(holdingDetails));
    }

    /**
     * 修改持仓明细
     */
    @Operation(summary = "修改持仓明细")
//    @RequiresPermissions("business:holdingDetails:edit")
    @Log(title = "持仓明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody HoldingDetailsDTO holdingDetailsDTO) {
        holdingDetailsDTO.setUserId(SecurityUtils.getUserId());
        HoldingDetails holdingDetails = holdingDetailsStructMapper.toEntity(holdingDetailsDTO);
        return toAjax(holdingDetailsService.updateById(holdingDetails));
    }

    /**
     * 删除持仓明细
     */
    @Operation(summary = "删除持仓明细")
//    @RequiresPermissions("business:holdingDetails:remove")
    @Log(title = "持仓明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(holdingDetailsService.removeBatchByIds(Arrays.asList(ids)));
    }

    @Operation(summary = "计算持仓明细统计")
    @GetMapping("/statistics")
    public R<List<ProductPieDTO>> statistics() {
        HoldingDetails holdingDetails = new HoldingDetails();
        holdingDetails.setUserId(SecurityUtils.getUserId());
        List<HoldingDetails> list = holdingDetailsService.list(new QueryWrapper<>(holdingDetails));
        List<ProductPieDTO> productPieDTOList = new ArrayList<>();
        //统计每种类型的持仓金额
        Map<String, Double> map = list.stream().collect(
            Collectors.groupingBy(HoldingDetails::getType, Collectors.summingDouble(d -> d.getQuantity().doubleValue())));
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            ProductPieDTO productPieDTO = new ProductPieDTO();
            productPieDTO.setType(entry.getKey());
            productPieDTO.setTotal(entry.getValue());
            productPieDTOList.add(productPieDTO);
        }
        return R.ok(productPieDTOList);
    }

    @Operation(summary = "获取产品柱状图")
    @GetMapping("/getProductBarDTOList")
    public R<List<ProductBarDTO>> getProductBarDTOList() {
        HoldingDetails holdingDetails = new HoldingDetails();
        holdingDetails.setUserId(SecurityUtils.getUserId());
        holdingDetails.setType("fund");
        List<HoldingDetails> list = holdingDetailsService.list(new QueryWrapper<>(holdingDetails));
        System.out.println("获得数据："+list);
        List<ProductBarDTO> productBarDTOList = new ArrayList<>();
        for (HoldingDetails details : list) {
            ProductBarDTO productBarDTO = new ProductBarDTO();
            productBarDTO.setName(details.getName());
            productBarDTO.setTotal(details.getQuantity().doubleValue());
            productBarDTOList.add(productBarDTO);
        }
        return R.ok(productBarDTOList);
    }
}