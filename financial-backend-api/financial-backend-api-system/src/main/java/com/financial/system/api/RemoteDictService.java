package com.financial.system.api;

import com.financial.common.core.constant.ServiceNameConstants;
import com.financial.common.core.domain.R;
import com.financial.system.api.domain.SysDictData;
import com.financial.system.api.factory.RemoteDictFallbackFactory;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 字典服务
 * @author xinyi
 */
@Lazy
@FeignClient(contextId = "remoteDictService", value = ServiceNameConstants.SYSTEM_SERVICE, path = "/dict/data", fallbackFactory = RemoteDictFallbackFactory.class)
public interface RemoteDictService {

  @GetMapping(value = "/type/{dictType}")
  R<List<SysDictData>> dictType(@PathVariable String dictType);
}
