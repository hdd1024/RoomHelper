package com.hmz.aptroom;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hmz.aptroom.entity.TestEntity;
import com.hmz.extendroom_annotation.ExDao;

import java.util.List;

@Dao
public interface TestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TestEntity entity);

    @Query("select * from tb_testEntity")
    List<TestEntity> query();

}
