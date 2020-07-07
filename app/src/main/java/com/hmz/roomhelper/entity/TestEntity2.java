package com.hmz.roomhelper.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hmz.roomhelper_annotation.FieldHlp;

@Entity(tableName = "tb_testEntity2")
public class TestEntity2 {
    @PrimaryKey
    private int testId;
    @FieldHlp(oldFieldName = "testName0", startVersion = 1, endVersion = 2)
    private String testName;
    @FieldHlp(startVersion = 1, endVersion = 2)
    private double testD;
    @FieldHlp(startVersion = 2, endVersion = 3)
    private float testC;
    @FieldHlp(startVersion = 1, endVersion = 2)
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

