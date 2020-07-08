package com.hmz.roomhelper.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hmz.roomhelper_annotation.FieldHlp;

import java.util.Date;

@Entity(tableName = "tb_testEntity")
public class TestEntity {
    @PrimaryKey(autoGenerate = true)
    private int testId;
    //    @FieldHlp(startVersion = 2, endVersion = 3)
    private String testName;
    @FieldHlp(startVersion = 1, endVersion = 2)
    private long creatTime;
    @FieldHlp(startVersion = 2, endVersion = 3)
    private boolean isOk;

    @FieldHlp(startVersion = 3, endVersion = 4)
    private String noewDate;
    @FieldHlp(startVersion = 4, endVersion = 5)
    private double price;
    @FieldHlp(oldFieldName = "fff", oldStartVersion = 5, oldEndVersion = 6,
            startVersion = 6, endVersion = 7)
    private float ccc;

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

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getNoewDate() {
        return noewDate;
    }

    public void setNoewDate(String noewDate) {
        this.noewDate = noewDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public float getCcc() {
        return ccc;
    }

    public void setCcc(float ccc) {
        this.ccc = ccc;
    }
}
