package com.example.jbgf_room_annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***********************************************************
 * 创建时间:2020-06-21
 * 作   者: [hanmingze]
 * 功能描述: <用于正在生产Room的<code>@Database</code>标记类的时候
 * 作为<code>entities</code>的值>
 * 备注信息: {}
 * @see
 **********************************************************/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface JbEntity {

    /**
     * 使用该标记可以指定在可以在那个@JbRoomBase标记类生成xxx_JbRoomBase.java
     * 的时候处理该标签
     *
     * @return 默认返回""，代表所有@JbRoomBas注解自动生成xxx_JbRoomBase.java类
     * 的时候都可以处理该标签
     */
    String target() default "";
}
