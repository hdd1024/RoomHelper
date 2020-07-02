package com.hmz.extendroom;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hmz.extendroom.entity.TestEntity2;
import com.hmz.extendroom.roombase2.TestJbRoomBase2;
import com.hmz.extendroom_annotation.ExDao;

import java.util.List;

@ExDao(target = TestJbRoomBase2.TARGET)
@Dao
public interface TestDao2 {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TestEntity2 entity2);

    @Query("select * from tb_testEntity2")
    List<TestEntity2> query();
}
