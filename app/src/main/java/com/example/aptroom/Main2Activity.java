package com.example.aptroom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aptroom.entity.TestEntity;
import com.example.aptroom.roombase.TestJbRoomBase;
//import com.example.aptroom.roombase.TestJbRoomBase_JbRoomBase;

import java.util.List;

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
        TestJbRoomBase testJbRoomBase = new TestJbRoomBase();
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
