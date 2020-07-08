package com.hmz.roomhelper.controller;


import com.hmz.roomhelper.dao.TestDao2;
import com.hmz.roomhelper.entity.TestEntity2;
import com.hmz.roomhelper_annotation.DaoHlp;

public class TestController2 {

    @DaoHlp
    TestDao2 testDao2;

    public void insert() {
        TestEntity2 testEntity2 = new TestEntity2();
        testEntity2.setTestName("王二小");
        testEntity2.setTtt("哈哈哈");

        testDao2.insert(testEntity2);
    }
}
