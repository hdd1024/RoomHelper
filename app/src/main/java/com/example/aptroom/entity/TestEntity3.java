package com.example.aptroom.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.aptroom.roombase2.TestJbRoomBase2;
import com.example.jbgf_room_annotation.JbEntity;
@JbEntity(target = TestJbRoomBase2.TARGET)
@Entity(tableName = "tb_testEntity3")
public class TestEntity3 {
    @PrimaryKey(autoGenerate = true)
    private int testId;
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
