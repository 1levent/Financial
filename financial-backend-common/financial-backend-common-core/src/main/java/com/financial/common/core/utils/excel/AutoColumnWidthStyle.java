package com.financial.common.core.utils.excel;

import cn.idev.excel.metadata.Head;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;

/**
 * FastExcel方案 - 通过自定义处理器实现自适应
 * @author xinyi
 */
public class AutoColumnWidthStyle extends AbstractColumnWidthStyleStrategy {

  // 默认配置参数
  // 中文字符宽度系数
  private static final float CHINESE_FACTOR = 2.2f;
  // 其他字符宽度系数
  private static final float OTHER_FACTOR = 1.1f;
  // 最大列宽字符数
  private static final int MAX_WIDTH = 100;

  // 列宽缓存
  private final Map<Integer, Integer> maxWidthMap = new HashMap<>();

  @Override
  protected void setColumnWidth(WriteSheetHolder holder,
      List<cn.idev.excel.metadata.data.WriteCellData<?>> cellDataList,
      Cell cell, Head head, Integer rowIndex, Boolean isHead) {
    int colIdx = cell.getColumnIndex();
    String text = cell.getStringCellValue();

    // 计算有效宽度
    int width = calculateEffectiveWidth(text);
    int maxAllowed = (int) (MAX_WIDTH * 256);
    int finalWidth = Math.min(width, maxAllowed);

    // 更新最大列宽
    maxWidthMap.put(colIdx, Math.max(
        maxWidthMap.getOrDefault(colIdx, 0),
        finalWidth
    ));
  }
  /**
   * 计算有效列宽(核心算法)
   */
  private int calculateEffectiveWidth(String text) {
    long chineseCount = text.chars()
        .filter(c -> Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN)
        .count();
    int otherCount = text.length() - (int)chineseCount;
    return (int) (
        chineseCount * CHINESE_FACTOR * 256 +
            otherCount * OTHER_FACTOR * 256
    );
  }
}