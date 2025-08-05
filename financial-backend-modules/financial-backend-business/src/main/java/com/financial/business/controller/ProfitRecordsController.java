package com.financial.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.business.entity.HoldingDetails;
import com.financial.business.entity.ProfitRecords;
import com.financial.business.entity.conveter.ProfitRecordsStructMapper;
import com.financial.business.entity.dto.ProfitRecordsDTO;
import com.financial.business.entity.dto.statistic.ProductProfitDTO;
import com.financial.business.entity.dto.statistic.ProfitDayDataDTO;
import com.financial.business.service.IHoldingDetailsService;
import com.financial.business.service.IProfitRecordsService;
import com.financial.common.core.domain.R;
import com.financial.common.core.utils.DateUtils;
import com.financial.common.core.utils.StringUtils;
import com.financial.common.core.utils.excel.ExcelUtils;
import com.financial.common.core.web.controller.BaseController;
import com.financial.common.core.web.domain.AjaxResult;
import com.financial.common.core.web.page.PageResponse;
import com.financial.common.core.web.page.TableDataInfo;
import com.financial.common.log.annotation.Log;
import com.financial.common.log.enums.BusinessType;
import com.financial.common.mq.service.MessagePublisher;
import com.financial.common.security.annotation.RequiresPermissions;
import com.financial.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.aspectj.weaver.loadtime.Aj;
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

    @Resource
    private IHoldingDetailsService holdingDetailsService;


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
    public R<ProfitRecordsDTO> getInfo(@PathVariable Long id) {
        ProfitRecords profitRecords = profitRecordsService.getById(id);
        if (profitRecords == null) {
            return R.fail("该记录不存在");
        }
        ProfitRecordsDTO profitRecordsDTO = profitRecordsStructMapper.toDto(profitRecords);
        return R.ok(profitRecordsDTO);
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
        if(StringUtils.isNull(profitRecordsDTO.getHoldingDetailId())){
            AjaxResult.error("持仓id不能为空");
        }
        HoldingDetails holdingDetails = holdingDetailsService.getById(profitRecordsDTO.getHoldingDetailId());
        //根据涨跌幅计算具体收益
        BigDecimal priceChg = holdingDetails.getQuantity().multiply(profitRecordsDTO.getChg());
        profitRecordsDTO.setProfitAmount(priceChg);
        //根据收益更新持仓
        holdingDetails.setQuantity(holdingDetails.getQuantity().add(priceChg));
        if(!holdingDetailsService.updateById(holdingDetails)){
            return AjaxResult.warn("修改持仓明细失败");
        }
        //获取现在的时间作为交易时间
        profitRecordsDTO.setProfitTime(new Date());
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

    /**
     * 收益统计与分析
     */
    @GetMapping("/calculateProfit")
    public void calculateProfit(){
        //获取当前用户
        Long userId = SecurityUtils.getUserId();
        //获取当前用户的所有收益
        List<ProfitRecords> profitRecords = profitRecordsService.list(new QueryWrapper<ProfitRecords>().eq("user_id", userId));
        Map<String, Object> map = new HashMap<>();

        //展示一个条形图，横轴为产品名称，纵轴为收益金额

    }

    @GetMapping("/getProfitDayData")
    @Operation(summary = "获取收益日数据")
    public R<List<ProfitDayDataDTO>> getProfitDayData(){
        List<ProfitDayDataDTO> profitDayDataDTOList = new ArrayList<>();
        //获取当前用户
        Long userId = SecurityUtils.getUserId();
        //获取当前用户的所有收益
        List<ProfitRecords> profitRecords = profitRecordsService.list(new QueryWrapper<ProfitRecords>().eq("user_id", userId));
        System.out.println("获取到的数据："+profitRecords);
        //根据日期来统计
        //首先是展示日数据，统计每日的收益
        Map<String, List<ProfitRecords>> map = profitRecords.stream().collect(Collectors.groupingBy(item -> {
            //获取日期
            return DateFormatUtils.format(item.getProfitTime(), "yyyy-MM-dd");
        }));
        for (Map.Entry<String, List<ProfitRecords>> entry : map.entrySet()) {
            ProfitDayDataDTO profitDayDataDTO = new ProfitDayDataDTO();
            profitDayDataDTO.setDate(entry.getKey());
            profitDayDataDTO.setValue(entry.getValue().stream().map(item -> item.getProfitAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
            profitDayDataDTOList.add(profitDayDataDTO);
        }
        System.out.println("分组后的数据："+profitDayDataDTOList);
        return R.ok(profitDayDataDTOList);
    }

    @GetMapping("/getProductProfitDTOList")
    @Operation(summary = "获取产品收益图")
    public R<List<ProductProfitDTO>> getProductProfitDTOList(){
        List<ProductProfitDTO> productProfitDTOList = new ArrayList<>();
        //获取当前用户
        Long userId = SecurityUtils.getUserId();
        //获取当前用户的所有收益
        List<ProfitRecords> profitRecords = profitRecordsService.list(new QueryWrapper<ProfitRecords>().eq("user_id", userId));
        //根据名称聚合收益
        Map<String, List<ProfitRecords>> map = new HashMap<>();
        for (ProfitRecords profitRecord : profitRecords) {
            HoldingDetails product = holdingDetailsService.getById(profitRecord.getHoldingDetailId());
            if (map.containsKey(product.getName())) {
                map.get(product.getName()).add(profitRecord);
            } else {
                List<ProfitRecords> list = new ArrayList<>();
                list.add(profitRecord);
                map.put(product.getName(), list);
            }
        }
        for (Map.Entry<String, List<ProfitRecords>> entry : map.entrySet()) {
            ProductProfitDTO productProfitDTO = new ProductProfitDTO();
            productProfitDTO.setName(entry.getKey());
            productProfitDTO.setTotal(entry.getValue().stream().map(item -> item.getProfitAmount()).reduce(BigDecimal.ZERO, BigDecimal::add));
            productProfitDTOList.add(productProfitDTO);
        }
        return R.ok(productProfitDTOList);
    }

}