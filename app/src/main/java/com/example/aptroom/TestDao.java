package com.example.aptroom;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.aptroom.entity.TestEntity;
import com.example.jbgf_room_annotation.JbDao;

import java.util.List;

@JbDao
@Dao
public interface TestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TestEntity entity);

    @Query("select * from tb_testEntity")
    List<TestEntity> query();

}
