package com.financial.common.security.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.financial.common.security.config.DictSerializer;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义字典注解实现自动转换
 * @author xinyi
 */
@JacksonAnnotationsInside
@JsonSerialize(using = DictSerializer.class)
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dict {
  //字典类型
  String type();
}
