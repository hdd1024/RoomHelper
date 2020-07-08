package com.hmz.roomhelper_api;

import android.content.Context;

import androidx.room.RoomDatabase;

import java.util.HashMap;
import java.util.Map;

/***********************************************************
 * 创建时间:2020-07-07
 * 作   者: [hanmingze]
 * describe: <RoomHelper的辅助类>
 * 备注信息: {}
 * @see
 **********************************************************/
public class RoomHelper {
    private final static String TAG = RoomHelper.class.getSimpleName();
    private static RoomHelper intanse;
    private Object databaseObj;
    private Class base_Hlp_Class;
    private Map<String, Object> controllerMap;
    private HelperBuilder helperBuilder;

    public static RoomHelper getIntanse() {
        if (intanse == null) {
            synchronized (RoomHelper.class) {
                if (intanse == null) {
                    intanse = new RoomHelper();
                }
            }
        }
        return intanse;
    }

    private RoomHelper() {
        controllerMap = new HashMap<>();
    }

    public RoomHelper initHelper(Object databaseObj) {
        this.databaseObj = databaseObj;
        Class databaseClass = databaseObj.getClass();
        String canonicalName = databaseClass.getCanonicalName();
        String base_Hlp_Name = canonicalName + "_Hlp";
        try {
            base_Hlp_Class = Class.forName(base_Hlp_Name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intanse;
    }

    /**
     * 该方用于配置数据库迁移，会自动将xxx.TestJbRoomBase_Hlp类中生成
     * MIGRATION_1_2 添加到builder.addMigrations()方法中
     *
     * @param context 上下文
     * @return 添加完毕后返回条件好的Builder
     */
    public <T extends RoomDatabase> HelperBuilder<T> migrateConfig(Context context) {
        AnalyzingMigrate migrate = new AnalyzingMigrate(databaseObj, base_Hlp_Class);
        try {
            helperBuilder = new HelperBuilder(migrate.getBuilder(context));
            return helperBuilder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T injectDao(Class<T> controllerClass) {
        RoomDatabase roomDatabase = helperBuilder.getRoomDatabase();
        if (roomDatabase == null) throw new NullPointerException("不能获取到roomDatabase，使用" +
                "该方法需要配合migrateConfig()一期使用！");
        return injectDao(controllerClass, roomDatabase);
    }

    /**
     * 为一个Controller类注入Dao，并且会返回这个操作数据库类的实例
     *
     * @param controllerClass 操作数据了的class对象
     * @return 返回该class的实例
     */
    public <T> T injectDao(Class<T> controllerClass, Object database) {
//        if (migrate == null) throw new NullPointerException("请先调用了initHelper()初始化了工具类在调用此方法");
        String className = controllerClass.getName();
        Object controller = controllerMap.get(className);
        if (controller != null) {
            return controllerClass.cast(controller);
        }
        AnalyzingDaoHlpMap daoHlpMap = new AnalyzingDaoHlpMap(database);
        T t = daoHlpMap.injectDao(controllerClass);
        controllerMap.put(className, t);
        return t;
    }

    public void clearControllerMap() {
        controllerMap.clear();
    }
    public <T> T removeController(Class<T> controller) {
        Object remove = controllerMap.remove(controller.getName());
        return controller.cast(remove);
    }

    public void clear() {
        databaseObj = null;
        helperBuilder = null;
        base_Hlp_Class = null;
        controllerMap.clear();
        controllerMap = null;
        intanse = null;

    }

}
