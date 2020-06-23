package com.hmz.aptroom;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hmz.aptroom.entity.TestEntity3;
import com.hmz.aptroom.roombase2.TestJbRoomBase2;
import com.hmz.extendroom_annotation.ExDao;

import java.util.List;

@ExDao(target = TestJbRoomBase2.TARGET)
@Dao
public interface TestDao3 {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TestEntity3 entity3);

    @Query("select * from tb_testEntity3")
    List<TestEntity3> query();
}
