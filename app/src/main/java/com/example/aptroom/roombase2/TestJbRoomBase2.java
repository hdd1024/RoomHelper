package com.example.aptroom.roombase2;

import android.content.Context;

//import com.example.aptroom.roombase.TestJbRoomBase_JbRoomBase;
import com.example.jbgf_room_annotation.JbRoomBase;

@JbRoomBase(setTarget = TestJbRoomBase2.TARGET, version = 2)
public class TestJbRoomBase2 {
    public final static String TARGET = "com.example.aptroom.roombase2.TestJbRoomBase2";

//    private TestJbRoomBase_JbRoomBase jbRoomBase;
//
//    public TestJbRoomBase_JbRoomBase getBuilder(Context context) {
//        if (jbRoomBase == null) {
//            jbRoomBase = (TestJbRoomBase_JbRoomBase) TestJbRoomBase_JbRoomBase.roomInit(context)
//                    .fallbackToDestructiveMigration()
//                    .allowMainThreadQueries()   //可以在主线程操作
//                    .build();
//        }
//
//        return jbRoomBase;
//    }

//    public TestJbRoomBase_JbRoomBase getBuilder(Context context) {
//        if (jbRoomBase == null) {
//            jbRoomBase = Room.databaseBuilder(context,TestJbRoomBase_JbRoomBase.class,"jjjjjjjjj")
//                    .fallbackToDestructiveMigration()
//                    .allowMainThreadQueries()   //可以在主线程操作
//                    .build();
//        }
//
//        return jbRoomBase;
//    }
}
