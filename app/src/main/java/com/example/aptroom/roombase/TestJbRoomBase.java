package com.example.aptroom.roombase;

import android.content.Context;
import androidx.room.Room;
import com.example.jbgf_room_annotation.JbRoomBase;

@JbRoomBase(version = 2)
public class TestJbRoomBase {

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
