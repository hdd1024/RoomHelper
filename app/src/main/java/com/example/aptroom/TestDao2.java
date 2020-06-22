package com.example.aptroom;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.aptroom.entity.TestEntity2;
import com.example.aptroom.roombase2.TestJbRoomBase2;
import com.example.jbgf_room_annotation.JbDao;

import java.util.List;

@JbDao(target = TestJbRoomBase2.TARGET)
@Dao
public interface TestDao2 {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TestEntity2 entity2);

    @Query("select * from tb_testEntity2")
    List<TestEntity2> query();
}
