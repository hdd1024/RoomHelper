package com.hmz.roomhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.hmz.roomhelper.controller.TestController;
import com.hmz.roomhelper.entity.TestEntity;
import com.hmz.roomhelper.roombase.TestJbRoomBase;
//import com.example.aptroom.roombase.TestJbRoomBase_JbRoomBase;


public class Main2Activity extends AppCompatActivity {

    Button btn_db_query;
    TextView tv_db_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn_db_query = findViewById(R.id.btn_db_query);
        tv_db_data = findViewById(R.id.tv_db_data);
        TestEntity entity = new TestEntity();
        entity.setTestId(12345);
        entity.setTestName("=====韩明泽哦！");
        TestJbRoomBase.getInstance().init(getBaseContext());
        TestController testController = TestJbRoomBase.getInstance().getTestController();
//        testController.intsert();


//        final TestJbRoomBase_JbRoomBase builder = testJbRoomBase.getBuilder(getBaseContext());
//        TestDao testDao = builder.getTestDao();

//        testDao.insert(entity);
//        btn_db_query.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                TestDao testDao = builder.getTestDao();
//                List<TestEntity> entities = testDao.query();
//                for (TestEntity en : entities) {
//                    tv_db_data.setText("查询的数据为：" + en.getTestName());
//                }
//
//            }
//        });

    }
}
