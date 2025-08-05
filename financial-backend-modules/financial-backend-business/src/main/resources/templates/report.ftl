<!-- templates/report.ftl -->
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>${title}</title>
  <style>
    .antd-table { border-collapse: collapse; }
    .antv-chart { margin: 20px; }
  </style>
</head>
<body>
<h1>${header}</h1>

<!-- 动态表格 -->
<table class="antd-table">
    <#list dataList as item>
      <tr>
        <td>${item.id}</td>
        <td>${item.name}</td>
      </tr>
    </#list>
</table>

<!-- 图表占位 -->
<div class="antv-chart">
    <#if chartEnabled>
      <img src="${chartUrl}" alt="数据分析图表">
    </#if>
</div>
</body>
</html>