package com.yingda.lkj.utils;

import com.yingda.lkj.beans.system.cache.CacheMap;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 内存接口，重启服务不会序列化
 */
@Slf4j
public class CacheUtil {

    private static final Map<String, Object> ROOT_MAP = new CacheMap<>();

    private static void init() {
        System.out.println("暂时没用");
    }

    public static Map<String, Object> createMap(String key) {
        Map<String, Object> cacheMap = new CacheMap<>();
        if (ROOT_MAP.get(key) != null)
            throw new RuntimeException(String.format("createMap使用的key重复,key:%s", key));

        ROOT_MAP.put(key, cacheMap);
        return cacheMap;
    }

    public static Map<String, Object> getMap(String key) {
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) ROOT_MAP.get(key);
        if (result == null) return createMap(key);
        return result;
    }

}
