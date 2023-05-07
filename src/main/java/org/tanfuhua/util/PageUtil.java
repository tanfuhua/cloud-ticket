package org.tanfuhua.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author gaofubo
 * @date 2023/5/4
 */
@UtilityClass
public class PageUtil {

    public static <T> IPage<T> createIPage(Integer page, Integer perPage) {
        return new Page<>(page, perPage);
    }

    public static <T> org.tanfuhua.common.response.Page<T> iPageToPage(IPage<T> iPage) {
        org.tanfuhua.common.response.Page<T> page = new org.tanfuhua.common.response.Page<T>();
        page.setItems(iPage.getRecords());
        page.setPage(iPage.getCurrent());
        page.setTotal(iPage.getTotal());
        return page;
    }

    public static <T, R> org.tanfuhua.common.response.Page<R> iPageToPage(IPage<T> iPage, Function<T, R> mapper) {
        org.tanfuhua.common.response.Page<R> page = new org.tanfuhua.common.response.Page<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            page.setItems(iPage.getRecords().stream().map(mapper).collect(Collectors.toList()));
        }
        page.setPage(iPage.getCurrent());
        page.setTotal(iPage.getTotal());
        return page;
    }

}
