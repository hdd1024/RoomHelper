//package com.hmz.roomhelper.dao;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//
//import com.hmz.roomhelper.entity.TestEntity3;
//
//import java.util.List;
//
//@Dao
//public interface TestDao3 {
//
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(TestEntity3 entity3);
//
//    @Query("select * from tb_testEntity3")
//    List<TestEntity3> query();
//}
