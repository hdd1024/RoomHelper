package com.hmz.roomhelper.controller;


import android.util.Log;

import com.hmz.roomhelper.dao.TestDao;
import com.hmz.roomhelper.dao.TestDao2;
import com.hmz.roomhelper.entity.TestEntity;
import com.hmz.roomhelper.entity.TestEntity2;
import com.hmz.roomhelper_api.ano.DaoController;

import java.util.List;

public class TestController {

    @DaoController
    TestDao testDao;
    @DaoController
    TestDao2 testDao2;

    public void intsert(String name, int id) {
        TestEntity testEntity = new TestEntity();
        testEntity.setTestId(id);
        testEntity.setTestName(name);
        testEntity.setCreatTime(System.currentTimeMillis());
        testDao.insert(testEntity);
        Log.d("aaaaaaaaa>>>>aaaa", testDao.toString());
    }

    public TestEntity queryById(int id) {
        TestEntity testEntity = testDao.queryById(id);
        return testEntity;
    }

    public void intsertDao2() {
        TestEntity2 testEntity2 = new TestEntity2();
        testEntity2.setTestId(888888);
        testEntity2.setTestName("麻花藤");
        testEntity2.setTtt("撸啊撸");
        testEntity2.setTestC(3.14f);
        testDao2.insert(testEntity2);
    }

    public List<TestEntity2> dao2QueryAll() {
        return testDao2.query();
    }
}
