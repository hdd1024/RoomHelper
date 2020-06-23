package com.hmz.aptroom.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hmz.extendroom_annotation.ExEntity;

@Entity(tableName = "tb_testEntity")
public class TestEntity {
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