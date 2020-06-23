package com.hmz.extendroom_annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***********************************************************
 * 创建时间:2020-06-23
 * 作   者: [hanmingze]
 * 功能描述: <该标签可以对已有的数据库进行增、删、改。>
 * 备注信息: {&增& 当该注解标注在一个属性上，并且该注解oldFileName什么值也没有设置的时候
 *           &改& 当该注解修饰在一个属性上，并且有设置该注解的{@link #oldFileName()}值 }
 * @see
 **********************************************************/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface ExAlter {

    /**
     * 设置该值，代表要对已有的表字段进行修改
     * 里面添加的值为修改前的字段名
     *
     * @return 修改前的字段名
     */
    String oldFileName() default "";

    /**
     * 修改从哪个版本号开始
     * @return 如果为0 则会以当前版本号为开始本版
     */
    int startVersion();

    /**
     * 修改以哪个版本结束
     * @return 如果为0 则会以当前版本号+1作为结束版本号
     */
    int endVersion();
}
