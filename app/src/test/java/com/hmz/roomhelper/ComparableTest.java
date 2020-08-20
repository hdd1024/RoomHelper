package com.hmz.roomhelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComparableTest {


//    public static void main(String[] args) {
//        List<XiaoMing> xiaoMingList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            XiaoMing xiaoMing = new XiaoMing();
//            xiaoMing.setAge(18 + i);
//            xiaoMing.setName("小明" + i);
//            xiaoMingList.add(xiaoMing);
//        }
//        //年龄最大的小明
//        XiaoMing maxXiaoMing = Collections.max(xiaoMingList);
//        //年龄最小的小明
//        XiaoMing minXiaoMing = Collections.min(xiaoMingList);
//    }


    public static void main(String[] args) {
        BBBB bbbb = new BBBB();
        bbbb.setIII(new IIIII() {
            @Override
            public void testIII() {

            }
        }, "王二小");
        bbbb.setIII(new IIIII() {
            @Override
            public void testIII() {

            }
        }, "王二小-----");


        bbbb.ccc();
    }
}
