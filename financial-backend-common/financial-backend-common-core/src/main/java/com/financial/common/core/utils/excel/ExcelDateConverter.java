package com.financial.common.core.utils.excel;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.exception.ExcelDataConvertException;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期格式转换器
 *
 * @author xinyi
 */
public class ExcelDateConverter implements Converter<LocalDate> {
    private static final Logger log = LoggerFactory.getLogger(ExcelDateConverter.class);

    // 使用线程安全的DateTimeFormatter（网页4推荐方案）
    private static final List<DateTimeFormatter> DATE_FORMATS = Arrays.asList(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyyMMdd"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
        DateTimeFormatter.ISO_LOCAL_DATE
    );

    @Override
    public LocalDate convertToJavaData(ReadCellData<?> cellData,
        ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration) {
        try {
            if (cellData.getType() == CellDataTypeEnum.NUMBER) {
                // 修正Excel日期基数（网页3关键算法）
                double excelValue = cellData.getNumberValue().doubleValue();
                return handleExcelNumericDate(excelValue);
            } else if (cellData.getType() == CellDataTypeEnum.STRING) {
                return parseStringDate(cellData.getStringValue());
            }
        } catch (Exception e) {
            log.error("日期解析失败: {}", cellData, e);
        }
        throw new ExcelDataConvertException(cellData.getRowIndex(), cellData.getColumnIndex(), cellData,contentProperty,"无法解析日期");
    }

    private LocalDate handleExcelNumericDate(double excelValue) {
        // Excel日期修正逻辑
        long days = (long) Math.floor(excelValue);
        if (days > 60) days--;

        return LocalDate.of(1900, 1, 1)
            .plusDays(days - 1);
    }

    private LocalDate parseStringDate(String dateStr) {
        // 多格式尝试解析
        for (DateTimeFormatter formatter : DATE_FORMATS) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException ignored) {
                // 尝试其他格式
            }
        }
        throw new DateTimeException("无法识别的日期格式: " + dateStr);
    }

    @Override
    public WriteCellData<?> convertToExcelData(LocalDate value,
        ExcelContentProperty contentProperty,
        GlobalConfiguration globalConfiguration) {
        // 统一输出为ISO格式
        return new WriteCellData<>(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}