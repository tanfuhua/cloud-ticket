package org.tanfuhua.common.response;

import lombok.Data;

import java.util.List;

/**
 * @author gaofubo
 * @date 2023/5/4
 */
@Data
public class Page<T> {

    /**
     * 数据
     */
    private List<T> items;

    /**
     * 总数
     */
    private Long total;

    /**
     * 页码
     */
    private Long page;

}
