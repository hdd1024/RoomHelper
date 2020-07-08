//package com.hmz.roomhelper.dao;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.OnConflictStrategy;
//import androidx.room.Query;
//
//import com.hmz.roomhelper.entity.TestAddTable;
//
//import java.util.List;
//
//@Dao
//public interface TestAddTableDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(TestAddTable entity);
//
//    @Query("select * from tb_testaddtable")
//    List<TestAddTable> query();
//}
