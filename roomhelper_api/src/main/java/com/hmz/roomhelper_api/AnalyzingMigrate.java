package com.hmz.roomhelper_api;

import android.content.Context;
import android.util.Log;

import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;

import com.hmz.roomhelper_api.utils.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/***********************************************************
 * 创建时间:2020-07-07
 * 作   者: [hanmingze]
 * describe: <该类用于解析MIGRATION_1_2变量>
 * 备注信息: {该类通过反射自动将xx.TestJbRoomBase_Hlp类中的MIGRATION_1_2变量
 * 添加到{@link RoomDatabase.Builder#addMigrations(Migration...)}方法中}
 **********************************************************/
public class AnalyzingMigrate {

    private RoomDatabase.Builder builder;
    private Object databaseObj;
    private Class base_Hlp_Impl_Class;

    public AnalyzingMigrate(Object databaseObj, Class base_Hlp_Impl_Class) {
        this.databaseObj = databaseObj;
        this.base_Hlp_Impl_Class = base_Hlp_Impl_Class;
    }

    public RoomDatabase.Builder getBuilder(Context context)
            throws Exception {
        RoomDatabase.Builder builder = null;
        //获取xx.TestJbRoomBase_Hlp类
        Method roomInit = base_Hlp_Impl_Class.getMethod("roomInit", Context.class);
        builder = (RoomDatabase.Builder) roomInit.invoke(null, context);
        Log.d("AnalyzingMigrate", "builder的值：" + builder);
        this.builder = builder;
        //解析TestJbRoomBase类中是否存在自定义的Migration变量
        Field[] declaredFields = base_Hlp_Impl_Class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Type fieldType = declaredField.getGenericType();
            if (fieldType.equals(Migration.class)) {
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                Migration migration = (Migration) declaredField.get(databaseObj);
                Log.d("AnalyzingMigrate", "------>migration的值：" + migration);

                if (builder != null) {
                    builder.addMigrations(migration);
                }
            }
        }
        //解析TestJbRoomBase_Hlp类中的Migration变量
        //获取父元素的属性 也就是获取xx.TestJbRoomBase_Hlp的属性
        Field[] hlpFields = base_Hlp_Impl_Class.getDeclaredFields();
        for (Field hlpField : hlpFields) {
            Type fieldType = hlpField.getGenericType();
            if (fieldType==Migration.class) {
                if (!hlpField.isAccessible()) {
                    hlpField.setAccessible(true);
                }
                builder.addMigrations((Migration) hlpField.get(null));
                Log.d(">>>>>>>>>>>aaaaaa>>>>>>", "nnnn<<<<" + hlpField.getName());
            }
        }
        return builder;
    }

    public RoomDatabase.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(RoomDatabase.Builder builder) {
        this.builder = builder;
    }
}
