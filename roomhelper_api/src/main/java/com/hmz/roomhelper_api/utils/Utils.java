package com.hmz.roomhelper_api.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class Utils {

    /**
     * 解析属性
     *
     * @param tClass 解析属性的类型
     * @param field  属性
     * @param obj    对象
     * @return 返回该属性的实例
     * @throws IllegalAccessException 异常
     */
    public static <T> T analyzingField(Class<T> tClass, Field field, Object obj)
            throws IllegalAccessException {
        Type fieldType = field.getGenericType();
        if (fieldType.equals(tClass)) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            Log.d(">>>>>>>>>>>aaaaaa>>>>>>", "nnnn<<<<" + field.getName());
            return tClass.cast(field.get(obj));
        }
        return null;
    }
}
