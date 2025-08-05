package com.financial.business.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.business.entity.BudgetManagement;
import com.financial.business.entity.IncomeExpenseRecords;
import com.financial.business.entity.ProfitRecords;
import com.financial.business.service.IAccountService;
import com.financial.business.service.IBudgetManagementService;
import com.financial.business.service.IIncomeExpenseRecordsService;
import com.financial.business.service.IProfitRecordsService;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 接入大模型对话
 * @author xinyi
 */
@RestController
@RequestMapping("/business/chat")
public class ChatController {

  @Resource
  private IIncomeExpenseRecordsService incomeExpenseRecordsService;

  @Resource
  private IAccountService  accountService;

  @Resource
  private IBudgetManagementService budgetManagementService;

  @Resource
  private IProfitRecordsService profitRecordsService;

  private RestTemplate restTemplate = new RestTemplate();
  private ObjectMapper objectMapper = new ObjectMapper();

  private String url = "https://jeniya.cn/v1/chat/completions";
  @PostMapping("/message")
  public String chat(@RequestBody String message) {
    System.out.println("收到的请求:"+message);
    //这个message是一个对象，获取里面的message属性
    String message1 = message.substring(message.indexOf("message")+10,message.length()-2);
    System.out.println("message1:"+message1);

    StringBuilder sb = new StringBuilder();
    sb.append("Role:金融理财规划专家和智能理财顾问Background:用户希望在复杂的金融市场中通过智能理财小助手获得个性化的理财建议实现资产的稳健增值用户可能缺乏专业的金融知识需要一个能够提供全面精准且易于理解的理财指导的工具Profile:你是一位经验丰富的金融理财规划专家精通各类金融产品和投资策略同时具备智能理财顾问的能力能够结合用户的财务状况风险偏好和理财目标提供定制化的理财方案Skills:你拥有金融分析风险评估投资组合构建财务规划以及智能算法应用的综合能力能够精准地分析市场动态为用户提供实时且实用的理财建议Goals:为用户提供个性化的理财建议帮助用户制定合理的财务规划实现资产的稳健增值Constrains:该理财建议应基于用户的实际财务状况和风险承受能力确保建议的可行性和安全性同时应避免使用过于复杂的金融术语使用户能够轻松理解和操作OutputFormat:简洁的文本格式直接提供关键信息和建议避免复杂格式和图表Workflow:1.收集用户的财务状况信息包括收入支出资产和负债等2.根据用户的风险偏好和理财目标分析适合的投资产品和策略3.提供个性化的理财建议包括投资组合构建风险评估和预期收益分析4.持续跟踪市场动态为用户提供定期的理财方案调整建议Examples:例子1用户A月收入1万元每月支出5000元有10万元闲置资金风险偏好较低建议将闲置资金分配为50%定期存款年利率2%30%货币基金预期年化收益率3%20%债券基金预期年化收益率4%预计年收益约3500元风险较低适合稳健型投资者例子2用户B月收入2万元每月支出1万元有50万元闲置资金风险偏好较高建议将闲置资金分配为30%股票基金预期年化收益率8%40%混合基金预期年化收益率6%20%债券基金预期年化收益率4%10%黄金预期年化收益率3%预计年收益约2.7万元风险适中适合积极型投资者例子3用户C月收入5000元每月支出4000元有5万元闲置资金风险偏好中等建议将闲置资金分配为40%定期存款年利率2%30%货币基金预期年化收益率3%20%债券基金预期年化收益率4%10%股票基金预期年化收益率8%预计年收益约1800元风险适中适合平衡型投资者Initialization:在第一次对话中请直接输出以下您好我是您的智能理财小助手我将根据您的财务状况和理财目标为您提供专业的理财建议请先告诉我您的月收入支出闲置资金以及风险偏好高、中、低我们可以开始为您量身定制理财方案Optimization:在回答用户问题时确保语言简洁明了避免冗长的解释使用清晰的结构和逻辑确保用户能够快速理解建议的核心内容同时提供具体的数字和数据支持增强建议的可信度Keywords:收入支出分析收益分析预算分析IncomeExpenseAnalysis:当用户提到收入支出分析时请直接提供每月可支配资金的统计结果并给出简短的节省开支建议例如每月可支配资金为5000元建议减少非必要支出增加储蓄比例InvestmentReturnAnalysis:当用户提到收益分析时请直接提供预期收益的简单统计例如预计年收益为3500元适合稳健型投资者BudgetAnalysis:当用户提到预算分析时请直接提供月度预算的简单统计例如建议将30%的资金用于储蓄30%用于投资40%用于日常开销。");
     //判断关键词
    if (message.contains("收入支出分析")) {
      //获取收支记录
      IncomeExpenseRecords incomeExpenseRecords = new IncomeExpenseRecords();
      incomeExpenseRecords.setUserId(104L);
      List<IncomeExpenseRecords> list = incomeExpenseRecordsService.list(
          new QueryWrapper<>(incomeExpenseRecords));
      sb.append("这是我的收入支出数据：").append(list.toString());
    }else if(message.contains("收益分析")){
      ProfitRecords profitRecords = new ProfitRecords();
      profitRecords.setUserId(104L);
      sb.append("这是我的收益数据：").append(profitRecordsService.list(new QueryWrapper<>(profitRecords)).toString());
    }else if(message.contains("预算分析")){
      BudgetManagement budgetManagement = new BudgetManagement();
      budgetManagement.setUserId(104L);
      sb.append("这是我的预算数据：").append(budgetManagementService.list(new QueryWrapper<>(budgetManagement)).toString());
    }else{
      sb.append("这是我的提问：").append(message1);
    }
    try {
      // 构造请求头
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", "Bearer sk-HAcE0fvKCQ5QtbXTYuUBe60IVneCk2JcblG5TXHAKKGZhaB1");
      headers.setContentType(MediaType.APPLICATION_JSON);

      // 构造请求体
      String requestBody = String.format("""
            {
                "model": "deepseek-chat",
                "messages": [
                    {
                        "role": "user",
                        "content": "%s"
                    }
                ],
                "temperature": 0.7
            }""", sb.toString());

      // 发送请求
      String response = restTemplate.postForObject(
          url,
          new HttpEntity<>(requestBody, headers),
          String.class
      );

      // 解析响应，提取content字段
      JsonNode rootNode = objectMapper.readTree(response);
      String content = rootNode.path("choices").get(0).path("message").path("content").asText();
      System.out.println("回答为："+content);
      return content;
    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred: " + e.getMessage();
    }
  }
}