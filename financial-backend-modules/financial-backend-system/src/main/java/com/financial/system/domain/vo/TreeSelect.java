package com.financial.system.domain.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.financial.system.domain.SysMenu;
import lombok.Data;

/**
 * Tree select树结构实体类
 * 
 * @author xinyi
 */
@Data
public class TreeSelect implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

}
