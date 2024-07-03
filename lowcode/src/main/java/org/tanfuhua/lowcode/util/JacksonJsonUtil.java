package org.tanfuhua.lowcode.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Json工具类
 *
 * @author: gaofubo
 * @date: 2021/3/31
 */
@UtilityClass
@Slf4j
public class JacksonJsonUtil {

    private static final ObjectMapper objectMapper;

    /**
     * 空数组Json串
     */
    public static final String EMPTY_ARRAY_JSON_STRING = "[]";
    /**
     * 空集合Json串
     */
    public static final String EMPTY_LIST_JSON_STRING = EMPTY_ARRAY_JSON_STRING;
    /**
     * 无内容的对象Json串
     */
    public static final String DEFAULT_OBJECT_JSON_STRING = "{}";

    static {
        ObjectMapper om;
        try {
            om = SpringUtil.getBean(ObjectMapper.class);
        } catch (Exception e) {
            om = new ObjectMapper();
        }
        objectMapper = om;
    }

    /**
     * 获取ObjectMapper
     *
     * @return ObjectMapper
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * 序列化
     * <p>
     * null -> "null"
     *
     * @param t   对象
     * @param <T> 泛型
     * @return String
     */
    public static <T> String toJsonString(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 带格式化的序列化
     * <p>
     * null -> "null"
     *
     * @param t   对象
     * @param <T> 泛型
     * @return String
     */
    public static <T> String toPrettyJsonString(T t) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化
     * "null" -> null
     * "{}" -> new T()
     * jsonString 不能为blank，否则会抛出异常
     *
     * @param jsonString jsonString
     * @param clazz      类型
     * @param <T>        泛型
     * @return T
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化
     * "null" -> null
     * "{}" -> new T()
     * jsonString 不能为blank，否则会抛出异常
     *
     * @param jsonString           jsonString
     * @param clazz                类型
     * @param defaultObjectIfError 异常时返回的对象
     * @param <T>                  泛型
     * @return T
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz, T defaultObjectIfError) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            return defaultObjectIfError;
        }
    }

    /**
     * 反序列化
     * jsonString 不能为blank，否则会抛出异常
     *
     * @param jsonString    jsonString
     * @param typeReference typeReference
     * @param <T>           泛型
     * @return T
     */
    public static <T> T parseObject(String jsonString, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析为Map对象:HashMap
     *
     * @param jsonString json文本
     * @return Map&lt;String, Object&gt;
     */
    public static Map<String, Object> parseToHashMap(String jsonString) {
        return parseObject(jsonString, new TypeReference<HashMap<String, Object>>() {
        });
    }

    /**
     * 解析为Map对象:LinkedHashMap
     *
     * @param jsonString json文本
     * @return Map&lt;String, Object&gt;
     */
    public static Map<String, Object> parseToLinkedHashMap(String jsonString) {
        return parseObject(jsonString, new TypeReference<LinkedHashMap<String, Object>>() {
        });
    }

    /**
     * 利用Json技术强转对象
     * 此方法适用于非集合对象
     *
     * @param targetObject 目标对象
     * @param tClass       目标类型
     * @return 强转后的类型对象
     */
    public static <T> T cast(Object targetObject, Class<T> tClass) {
        if (targetObject == null) {
            return null;
        }
        // 如果已经是目标类型，则没必要再利用Json技术强转
        if (tClass.isInstance(targetObject)) {
            return tClass.cast(targetObject);
        }
        // 如果是json串，直接解析
        if (targetObject instanceof String) {
            return parseObject((String) targetObject, tClass);
        }
        return parseObject(toJsonString(targetObject), tClass);
    }

    /**
     * 利用Json技术强转对象
     * 此方法适用于集合对象
     *
     * @param targetObject  目标对象
     * @param typeReference typeReference
     * @return 强转后的类型对象
     */
    public static <T> T cast(Object targetObject, TypeReference<T> typeReference) {
        if (targetObject == null) {
            return null;
        }
        // 如果是json串，直接解析
        if (targetObject instanceof String) {
            return parseObject((String) targetObject, typeReference);
        }
        return parseObject(toJsonString(targetObject), typeReference);
    }
}
