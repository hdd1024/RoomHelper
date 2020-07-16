package com.hmz.roomhelper_api;


import com.hmz.roomhelper_api.ano.DaoController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.Map;

/***********************************************************
 * 创建时间:2020-07-07
 * 作   者: [hanmingze]
 * describe: <解析xx.TestJbRoomBase_Hlp类中的DAO_FIELD_MAP变量>
 * 备注信息: {通过可以自动将{@link androidx.room.Dao}注入到操作数据库中
 * ，该类中必须含有@DaoHlp注解的属性}
 * @see
 **********************************************************/
public class AnalyzingDaoHlpMap {
    private static final String TAG = AnalyzingDaoHlpMap.class.getSimpleName();
    private Object base_Hlp_Imp_Obj;

    AnalyzingDaoHlpMap(Object base_Hlp_Imp_Obj) {
        this.base_Hlp_Imp_Obj = base_Hlp_Imp_Obj;
    }

    /**
     * 注入{@link androidx.room.Dao}注入到操作数据库的类中
     * 该方法主要通过反射方式获取到操作数据库类中@DaoHlp注解的属性
     * 然后会变量xx.TestJbRoomBase_Hlp类中的getxxxDao()方法
     * 获取到xxxDao_Imp的实例，并设置给@DaoHlp注解的属性
     *
     * @param contralClass 操作数据库的类
     * @param <T>          转化该数据库类型
     * @return 返回数据库操作数据的实例
     */
    <T> T injectDao(Class<T> contralClass) {
        Class<?> dataBaseObjClass = base_Hlp_Imp_Obj.getClass();
        boolean hasDao = false;
        T controllerInstance = null;
        try {
            for (Field declaredField : contralClass.getDeclaredFields()) {
                DaoController daoController = declaredField.getAnnotation(DaoController.class);
                if (daoController == null) continue;
                hasDao = true;
                //获取属性名称
                String fieldName = declaredField.getName();
                Field field = contralClass.getDeclaredField(fieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                //实例化操作数据库的类
                if (controllerInstance == null)
                    controllerInstance = contralClass.newInstance();
                Type fieldType = field.getGenericType();
                for (Method method : dataBaseObjClass.getSuperclass().getDeclaredMethods()) {
                    Type returnType = method.getGenericReturnType();
                    if (returnType.equals(fieldType)) {
                        Object invoke = method.invoke(base_Hlp_Imp_Obj, new Object[]{});
                        //类型转化
                        Object cast = field.getType().cast(invoke);
                        //赋值
                        field.set(controllerInstance, cast);
                    }
                }
            }
            if (hasDao)
                return controllerInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    <T> Map<String, T> injectDao() {
        Map<String, T> daoFieldMap = new HashMap<>();
        Class<?> dataBaseObjClass = base_Hlp_Imp_Obj.getClass();
        try {
            //获取保存@DaoHlp注解的属性的map
            Field dao_fields = dataBaseObjClass.getSuperclass().getDeclaredField("DAO_FIELD_MAP");
            if (!dao_fields.isAccessible()) {
                dao_fields.setAccessible(true);
            }
            Map<String, String> dao_field_map = (Map<String, String>) dao_fields.get(base_Hlp_Imp_Obj);
            for (String fieldKey : dao_field_map.keySet()) {
                String fieldName = dao_field_map.get(fieldKey);
                Class<?> fieldClass = Class.forName(fieldKey);
                //获取属性名称
                Field field = fieldClass.getDeclaredField(fieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                //实例化操作数据库的类
                T newInstance = (T) fieldClass.newInstance();
                Type fieldType = field.getGenericType();
                for (Method method : dataBaseObjClass.getSuperclass().getDeclaredMethods()) {
                    Type returnType = method.getGenericReturnType();
                    if (returnType.equals(fieldType)) {
                        Object invoke = method.invoke(base_Hlp_Imp_Obj, new Object[]{});
                        //类型转化
                        Object cast = field.getType().cast(invoke);
                        //赋值
                        field.set(newInstance, cast);
                    }
                }
                daoFieldMap.put(fieldKey, newInstance);
            }
            return daoFieldMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
