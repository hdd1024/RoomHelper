package com.hmz.roomhelper.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hmz.roomhelper_annotation.EntityHlp;

@EntityHlp(addTable = true, startVersion = 7, endVersion = 8)
@Entity(tableName = "tb_testEntity2")
public class TestEntity2 {
    @PrimaryKey
    private int testId;
    private String testName;
    private double testD;
    private float testC;
    private String ttt;


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

    public double getTestD() {
        return testD;
    }

    public void setTestD(double testD) {
        this.testD = testD;
    }

    public float getTestC() {
        return testC;
    }

    public void setTestC(float testC) {
        this.testC = testC;
    }

    public String getTtt() {
        return ttt;
    }

    public void setTtt(String ttt) {
        this.ttt = ttt;
    }
}

