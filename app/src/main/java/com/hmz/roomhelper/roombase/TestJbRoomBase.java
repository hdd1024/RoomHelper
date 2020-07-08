package com.hmz.roomhelper.roombase;

import android.content.Context;

import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;

import com.hmz.roomhelper.controller.TestController2;
import com.hmz.roomhelper_api.RoomHelper;
import com.hmz.roomhelper.controller.TestController;
import com.hmz.roomhelper_annotation.DatabaseHlp;

@DatabaseHlp(name = "test_roombase_db")
public class TestJbRoomBase {
    private static final TestJbRoomBase INSTANCE = new TestJbRoomBase();
    //只要使用了RoomHelper的migrateConfig()方法，下面这个就会自动添加
//    Migration MIGRATION_2_3 = new Migration(2, 3) {
//        @Override
//        public void migrate(androidx.sqlite.db.SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE  tb_testEntity2 ADD COLUMN testD REAL ");
//        }
//    };

    private TestJbRoomBase() {
    }

    public static TestJbRoomBase getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        RoomHelper.getIntanse().initHelper(this)
                .migrateConfig(context)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries() //可以在主线程操作
                .build();
    }

    public <T> T injectDao(Class<T> tClass) {
        return RoomHelper.getIntanse().injectDao(tClass);
    }

    public TestController getTestController() {
        return injectDao(TestController.class);
    }

    public TestController2 getTestController2() {
        return injectDao(TestController2.class);
    }


}
