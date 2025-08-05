package com.financial.common.core.utils.excel;

import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Excel工具类
 * @author xinyi
 */
public class ExcelUtils {

  // 使用线程安全的DateTimeFormatter
  private static final DateTimeFormatter TIMESTAMP_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");


  /**
   * 设置文件名
   * @param response
   * @param fileName
   * @return
   */
  public static HttpServletResponse setFileName(HttpServletResponse response, String fileName) {
    String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
    String encodedFileName = URLEncoder.encode(fileName + "_" + timestamp, StandardCharsets.UTF_8)
        .replaceAll("\\+", "%20");

    response.reset();
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setHeader("Content-Disposition",
        "attachment; filename*=" + encodedFileName + ".xlsx");
    return response;
  }

}
