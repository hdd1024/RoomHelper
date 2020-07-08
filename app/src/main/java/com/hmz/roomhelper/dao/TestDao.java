package com.hmz.roomhelper.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hmz.roomhelper.entity.TestEntity;

import java.util.List;

@Dao
public interface TestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TestEntity entity);

    @Query("select * from tb_testEntity")
    List<TestEntity> query();

    @Query("select * from tb_testEntity where testId=:id")
    TestEntity queryById(int id);

}
