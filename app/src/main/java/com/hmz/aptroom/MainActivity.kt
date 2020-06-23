package com.hmz.aptroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hmz.aptroom.roombase.TestJbRoomBase

class MainActivity : AppCompatActivity() {
    private var testJbRoomBase: TestJbRoomBase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        testJbRoomBase = TestJbRoomBase()
//        val testEntity: TestEntity = TestEntity()
//        testEntity.testId = 123
//        testEntity.testName = "韩明泽"
//        testJbRoomBase?.getBuilder(baseContext)?.testDao?.insert(testEntity)
//
//        btn_query.setOnClickListener {
//            val entitys = testJbRoomBase?.getBuilder(baseContext)?.testDao?.query()
//            entitys?.forEach {
//                tv_hello.setText(it.testName)
//            }
//        }
    }
}
