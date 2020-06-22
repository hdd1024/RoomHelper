package com.example.jbgf_room_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***********************************************************
 * 创建时间:2020-06-21
 * 作   者: [hanmingze]
 * 功能描述: <生成getxxxDao()抽象方法的标记>
 * 备注信息: {该注释用在Room的<code>@Dao</code>注释标记的类上，框架在编译期间会
 * 将使用该标记的类自动在{@link JbRoomBase 注释标记生成的类中生成 getxxxDao()的静态方法}}
 * @see
 **********************************************************/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface JbDao {
    /**
     * 使用该标记可以指定在可以在那个@JbRoomBase标记类生成xxx_JbRoomBase.java
     * 的时候处理该标签
     * @return 默认返回""，代表所有@JbRoomBas注解自动生成xxx_JbRoomBase.java类
     * 的时候都可以处理该标签
     */
    String target() default "";
}
