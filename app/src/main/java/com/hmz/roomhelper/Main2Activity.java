package com.hmz.roomhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hmz.roomhelper.controller.TestController;
import com.hmz.roomhelper.entity.TestEntity;
import com.hmz.roomhelper.roombase.TestJbRoomBase;


public class Main2Activity extends AppCompatActivity {

    Button btn_db_query;
    TextView tv_db_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn_db_query = findViewById(R.id.btn_db_query);
        tv_db_data = findViewById(R.id.tv_db_data);
        TestJbRoomBase.getInstance().init(getBaseContext());
        final TestController testController = TestJbRoomBase.getInstance().getTestController();
        testController.intsert("韩明泽", 12345);

        btn_db_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestEntity testEntity = testController.queryById(12345);
                tv_db_data.setText(testEntity.getTestName());
            }
        });

//        TestJbRoomBase.getInstance().getTestController2()
//                .insert();


    }
}
