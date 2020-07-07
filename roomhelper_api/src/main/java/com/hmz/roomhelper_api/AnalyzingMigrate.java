package com.hmz.roomhelper_api;

import android.content.Context;

import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;

import com.hmz.roomhelper_api.utils.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/***********************************************************
 * 创建时间:2020-07-07
 * 作   者: [hanmingze]
 * 功能描述: <该类用于解析MIGRATION_1_2变量>
 * 备注信息: {该类通过反射自动将xx.TestJbRoomBase_Hlp类中的MIGRATION_1_2变量
 * 添加到{@link RoomDatabase.Builder#addMigrations(Migration...)}方法中}
 **********************************************************/
public class AnalyzingMigrate {

    private RoomDatabase.Builder builder;
    private Object databaseObj;
    private Object base_Hlp_Imp_Obj;
    private Class databaseClass;
    private Class base_Hlp_Impl_Class;

    public AnalyzingMigrate(Object databaseObj, Object base_Hlp_Imp_Obj,
                            Class databaseClass, Class base_Hlp_Impl_Class) {
        this.databaseObj = databaseObj;
        this.base_Hlp_Imp_Obj = base_Hlp_Imp_Obj;
        this.databaseClass = databaseClass;
        this.base_Hlp_Impl_Class = base_Hlp_Impl_Class;
    }

    public RoomDatabase.Builder getBuilder(Context context)
            throws Exception {
        RoomDatabase.Builder builder = null;
        this.builder = builder;
        //获取xx.TestJbRoomBase_Hlp类
        Method roomInit = base_Hlp_Impl_Class.getMethod("roomInit", Context.class);
        builder = (RoomDatabase.Builder) roomInit.invoke(databaseObj, context);
        //解析TestJbRoomBase类中是否存在自定义的Migration变量
        Field[] declaredFields = databaseClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Type fieldType = declaredField.getGenericType();
            if (fieldType.equals(Migration.class)) {
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                Migration migration = (Migration) declaredField.get(databaseObj);
                if (builder != null) {
                    builder.addMigrations(migration);
                }
            }
        }
        //解析TestJbRoomBase_Hlp类中的Migration变量
        //获取父元素的属性 也就是获取xx.TestJbRoomBase_Hlp的属性
        Field[] hlpFields = base_Hlp_Impl_Class.getSuperclass().getDeclaredFields();
        for (Field hlpField : hlpFields) {
            Migration migration = Utils.analyzingField(Migration.class, hlpField, base_Hlp_Imp_Obj);
            if (migration != null)
                builder.addMigrations(migration);
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
