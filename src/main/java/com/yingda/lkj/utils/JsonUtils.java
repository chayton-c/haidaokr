package com.yingda.lkj.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author hood  2019/12/13
 */
@SuppressWarnings("unused")
public class JsonUtils {

    /**
     * json转list
     */
    public static <T> List<T> parseList(String jsonStr, Class<?> clas, String dateFormat) {
        Type type = new ParameterizedTypeImpl(clas);
        return new GsonBuilder().setDateFormat(dateFormat).create().fromJson(jsonStr, type);
    }

    /**
     * json转list
     */
    public static <T> List<T> parseList(String jsonStr, Class<?> clas) {
        Type type = new ParameterizedTypeImpl(clas);
        return new Gson().fromJson(jsonStr, type);
    }

    /**
     * json转pojo
     */
    public static <T> T parse(String jsonStr, Class<T> tClass) {
        return new Gson().fromJson(jsonStr, tClass);
    }


    private static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class<?> clazz;

        public ParameterizedTypeImpl(Class<?> clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
