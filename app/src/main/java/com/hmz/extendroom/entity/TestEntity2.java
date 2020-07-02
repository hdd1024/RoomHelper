package com.hmz.extendroom.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hmz.extendroom.roombase2.TestJbRoomBase2;
import com.hmz.extendroom_annotation.ExAlter;
import com.hmz.extendroom_annotation.ExEntity;

@ExEntity(target = TestJbRoomBase2.TARGET)
@Entity(tableName = "tb_testEntity2")
public class TestEntity2 {
    @PrimaryKey(autoGenerate = true)
    @ExAlter(oldFileName = "testId0", startVersion = 1, endVersion = 2)
    private int testId;
    @ExAlter(oldFileName = "testName0", startVersion = 1, endVersion = 2)
    private String testName;


    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}

