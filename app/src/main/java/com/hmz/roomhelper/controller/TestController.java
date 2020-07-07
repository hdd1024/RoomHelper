package com.hmz.roomhelper.controller;


import android.util.Log;


import com.hmz.roomhelper.dao.TestDao;
import com.hmz.roomhelper_annotation.DaoHlp;

public class TestController {

    @DaoHlp
    TestDao testDao;


    public void intsert() {
        Log.d("TestController", testDao.toString());
    }
}
