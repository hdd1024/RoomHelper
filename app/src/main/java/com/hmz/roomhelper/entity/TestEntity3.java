package com.hmz.roomhelper.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hmz.roomhelper_annotation.FieldHlp;

@Entity(tableName = "tb_testEntity3")
public class TestEntity3 {
    @PrimaryKey(autoGenerate = true)
    private int testId;
    @FieldHlp(startVersion = 1, endVersion = 2)
    private String testName;
    @FieldHlp(startVersion = 1, endVersion = 2)
    private String email;

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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
