package com.hmz.roomhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hmz.roomhelper.controller.TestController;
import com.hmz.roomhelper.entity.TestEntity;
import com.hmz.roomhelper.entity.TestEntity2;
import com.hmz.roomhelper.roombase.TestJbRoomBase;

import java.util.List;


public class Main2Activity extends AppCompatActivity {

    Button btn_db_query;
    TextView tv_db_data, tv_db_data2;
    private TestController testController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn_db_query = findViewById(R.id.btn_db_query);
        tv_db_data = findViewById(R.id.tv_db_data);
        tv_db_data2 = findViewById(R.id.tv_db_data2);
        TestJbRoomBase.getInstance().init(getBaseContext());
        testController = TestJbRoomBase.getInstance().getTestController();
        testController.intsert("韩明泽", 12345);

        testController.intsertDao2();

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

    public void dao2QueryAll(View view) {
        List<TestEntity2> testEntity2s = testController.dao2QueryAll();
        StringBuilder stringBuilder = new StringBuilder();
        for (TestEntity2 testEntity2 : testEntity2s) {
            stringBuilder.append(testEntity2.getTestName());
            stringBuilder.append("\n");
        }
        tv_db_data2.setText(stringBuilder.toString());
    }
}
