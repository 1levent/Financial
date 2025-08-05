package com.financial.business.service.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.business.entity.dto.data.FundBuyStatus;
import com.financial.business.entity.dto.data.FundDaily;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Comparator;

/**
 *
 * 股票等数据接口
 * @author xinyi
 */
@Service
public class AkShareDataService {

  private static final String AK_SHARE_URL ="http://localhost:8080/api/public/";
  private RestTemplate restTemplate = new RestTemplate();
  ObjectMapper mapper = new ObjectMapper();
  private static final String CACHE_KEY = "trade_dates";
  private static final ZoneId CN_ZONE = ZoneId.of("Asia/Shanghai");
  private static final DateTimeFormatter ISO_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
  // 使用Caffeine缓存，手动管理
  private static final Cache<String, List<LocalDate>> TRADE_DATE_CACHE =
      Caffeine.newBuilder()
          .maximumSize(1)
          .build();

  //获取基金每日净值接口
  public List<FundDaily> getFondDailyData() {
    try {
      String url = AK_SHARE_URL+"fund_open_fund_daily_em";
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
      List<Map<String, String>> rawList = mapper.readValue(response.getBody(),
          new TypeReference<List<Map<String, String>>>() {});

      return rawList.stream()
          .map(item -> new FundDaily(
              item.get("基金代码"),
              item.get("日增长率")
          ))
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch fund data", e);
    }
  }

//  @Test
  //获取基金申购状态
  public List<FundBuyStatus> getFundBuyStatus() {
    try {
      String url = AK_SHARE_URL+"fund_purchase_em";
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
      List<Map<String, String>> rawList = mapper.readValue(response.getBody(),
          new TypeReference<List<Map<String, String>>>() {});
      List<FundBuyStatus> fundBuyStatusList = new ArrayList<>();
      for (Map<String, String> item : rawList) {
        FundBuyStatus fundBuyStatus = new FundBuyStatus();
        fundBuyStatus.setCode(item.get("基金代码"));
        fundBuyStatus.setName(item.get("基金简称"));
        if(item.get("最新净值/万份收益") == null) {
          fundBuyStatus.setEquity(new BigDecimal("0"));
        }else{
          fundBuyStatus.setEquity(new BigDecimal(item.get("最新净值/万份收益")));
        }
        fundBuyStatus.setTime(item.get("最新净值/万份收益-报告时间"));
        fundBuyStatus.setBuyStatus(item.get("申购状态"));
        fundBuyStatus.setSellStatus(item.get("赎回状态"));
        fundBuyStatus.setNextOpenTime(item.get("下一开放日"));
        fundBuyStatus.setBuyBegin(new BigDecimal(item.get("购买起点")));
        fundBuyStatus.setBuyEnd(new BigDecimal(item.get("日累计限定金额")));
        if(item.get("手续费") == null){
          fundBuyStatus.setFee(new BigDecimal("0"));
        }else{
          fundBuyStatus.setFee(new BigDecimal(item.get("手续费")));
        }
        fundBuyStatusList.add(fundBuyStatus);
      }
      return fundBuyStatusList;
    }catch (Exception e){
      throw new RuntimeException("Failed to fetch fund data", e);
    }
  }

  /**
   * 获取交易日列表
   */
  @Test
  public void getTradeCalendar(){
    try {
      String url = AK_SHARE_URL+"tool_trade_date_hist_sina";
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
      List<Map<String, String>> tradeDatesRaw = mapper.readValue(response.getBody(),
          new TypeReference<List<Map<String, String>>>() {});
//      System.out.println("获取到的数据："+tradeDatesRaw);
      List<LocalDate> tradeDates = tradeDatesRaw.stream()
          .map(item -> LocalDate.parse(item.get("trade_date"), ISO_FORMATTER))
          .toList();
      //放入缓存
      TRADE_DATE_CACHE.put(CACHE_KEY, tradeDates);
    }catch (Exception e){
      throw new RuntimeException("Failed to fetch trade date", e);
    }
  }

  @Test
  public void test(){
    LocalDate neas = LocalDate.parse("2025-04-29", DateTimeFormatter.ISO_LOCAL_DATE);
    LocalDate nextTradeDay = getNextTradeDay();
    System.out.println("下一个交易日："+nextTradeDay);
    System.out.println("下一个交易日："+getNextTradeDay(neas));
  }


  // 获取下一个交易日
  public LocalDate getNextTradeDay() {
    List<LocalDate> sortedDates = TRADE_DATE_CACHE.getIfPresent(CACHE_KEY);
    if(sortedDates == null){
      getTradeCalendar();
    }
    sortedDates = TRADE_DATE_CACHE.getIfPresent(CACHE_KEY);
    LocalDate now = LocalDate.now(CN_ZONE).minusDays(1);

    // 使用二分查找优化性能
    int index = Collections.binarySearch(sortedDates, now, Comparator.naturalOrder());

    // 找到当前日期且是交易日
    if (index >= 0 && index < sortedDates.size() - 1) {
      return sortedDates.get(index + 1);
    }

    // 前日期之后的第一个大于它的日期
    int insertionPoint = (index >= 0) ? index + 1 : -index - 1;
    if (insertionPoint < sortedDates.size()) {
      return sortedDates.get(insertionPoint);
    }

    // 没有后续交易日
    return null;
  }

  // 获取指定日期的下一个交易日
  public LocalDate getNextTradeDay(LocalDate inputDate) {
    List<LocalDate> sortedDates = TRADE_DATE_CACHE.getIfPresent(CACHE_KEY);
    if(sortedDates == null){
      getTradeCalendar();
    }
    sortedDates = TRADE_DATE_CACHE.getIfPresent(CACHE_KEY);
    inputDate.atStartOfDay(CN_ZONE);

    // 使用二分查找优化性能
    int index = Collections.binarySearch(sortedDates, inputDate, Comparator.naturalOrder());

    // 找到当前日期且是交易日
    if (index >= 0 && index < sortedDates.size() - 1) {
      return sortedDates.get(index + 1);
    }

    // 前日期之后的第一个大于它的日期
    int insertionPoint = (index >= 0) ? index + 1 : -index - 1;
    if (insertionPoint < sortedDates.size()) {
      return sortedDates.get(insertionPoint);
    }

    // 没有后续交易日
    return null;
  }
}
