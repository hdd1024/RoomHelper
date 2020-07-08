package com.hmz.roomhelper.controller;


import android.util.Log;

import com.hmz.roomhelper.dao.TestDao;
import com.hmz.roomhelper.entity.TestEntity;
import com.hmz.roomhelper_annotation.DaoHlp;

public class TestController {

    @DaoHlp
     TestDao testDao;


    public void intsert(String name, int id) {
        TestEntity testEntity = new TestEntity();
        testEntity.setTestId(id);
        testEntity.setTestName(name);
        testEntity.setCreatTime(System.currentTimeMillis());
        testDao.insert(testEntity);
        Log.d("aaaaaaaaa>>>>aaaa",testDao.toString());
    }

    public TestEntity queryById(int id) {
        TestEntity testEntity = testDao.queryById(id);
        return testEntity;
    }
}
