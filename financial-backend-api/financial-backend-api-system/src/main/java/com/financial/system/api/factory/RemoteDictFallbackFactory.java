package com.financial.system.api.factory;

import com.financial.common.core.domain.R;
import com.financial.system.api.RemoteDictService;
import com.financial.system.api.domain.SysDictData;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 字典服务降级
 * @author xinyi
 */
@Component
public class RemoteDictFallbackFactory implements FallbackFactory<RemoteDictService> {
  private static final Logger log = LoggerFactory.getLogger(RemoteDictFallbackFactory.class);
  @Override
  public RemoteDictService create(Throwable cause) {
    log.error("字典服务调用失败:{}", cause.getMessage());
    return new RemoteDictService() {
      @Override
      public R<List<SysDictData>> dictType(String dictType) {
        return R.fail("获取字典失败:" + cause.getMessage());
      }
    };
  }
}
