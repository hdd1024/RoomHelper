package com.hmz.roomhelper.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hmz.roomhelper_annotation.FieldHlp;

@Entity(tableName = "tb_testEntity")
public class TestEntity {
    @PrimaryKey(autoGenerate = true)
    private int testId;
    @FieldHlp(startVersion = 2, endVersion = 3)
    private String testName;
    @FieldHlp(startVersion = 3, endVersion = 4)
    private long creatTime;

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

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }
}
