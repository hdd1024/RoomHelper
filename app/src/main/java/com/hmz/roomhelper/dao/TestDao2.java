package com.hmz.roomhelper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hmz.roomhelper.entity.TestEntity2;

import java.util.List;

@Dao
public interface TestDao2 {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TestEntity2 entity2);

    @Query("select * from tb_testEntity2")
    List<TestEntity2> query();
}
