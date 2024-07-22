package org.tanfuhua.lowcode.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author: gaofubo
 * @date: 2021/3/11
 */
@UtilityClass
public class FunctionUtil {

    public static <T> BinaryOperator<T> useNewValue() {
        return (ov, nv) -> nv;
    }

    /**
     * 将集合转为Map
     */
    public static <T, K, V> Map<K, V> convertCollToMap(Collection<T> coll,
                                                       Function<T, K> kMapper,
                                                       Function<T, V> vMapper) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyMap();
        }
        return coll.stream().collect(Collectors.toMap(kMapper, vMapper, useNewValue()));
    }

    /**
     * 将集合转为Map
     */
    public static <T, K, V, M extends Map<K, V>> Map<K, V> convertCollToMap(Collection<T> coll,
                                                                            Function<T, K> kMapper,
                                                                            Function<T, V> vMapper,
                                                                            Supplier<M> mapSupplier) {
        if (CollectionUtils.isEmpty(coll)) {
            return Collections.emptyMap();
        }
        return coll.stream().collect(Collectors.toMap(kMapper, vMapper, useNewValue(), mapSupplier));
    }

}
