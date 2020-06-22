package com.example.jbgf_room_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***********************************************************
 * 创建时间:2020-06-21
 * 作   者: [hanmingze]
 * 功能描述: <用于生成Room的@Database修饰的抽象类>
 * 备注信息: {在类似加上该标记，在编译期间会生产/app/build/generated
 * /ap_generated_sources/debug/out/标记类类路径/xxx_JbRoomBase.java
 * 文件。生成个该文件是对Room的<code>@Database</code>的标记使用。启动会生产
 * 一个<code>roomInit()</code>方法该方法会返回一个<code>RoomDatabase.Builder</code>
 * 可用于继续配置数据库}
 * @see
 **********************************************************/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface JbRoomBase {
    /**
     * 设置标记，该标记用匹配<code>target</code>标记。
     * 如果{@link JbEntity},{@link JbDao}注解中的填写的<code>target</code>
     * 和该标记的值一样，那么在自动生成xxx_JbRoomBase的时候会将@link JbEntity},{@link JbDao}
     * 标记的类添加到<code>@Database</code>的entities集合中，或者在xxx_JbRoomBase中自动
     * 生成getxxxDao()的方法。
     * @return 如果设置改值，但是{@link JbEntity},{@link JbDao}的也设置target，但是与该值不匹配
     * 那么以标记@JbRoomBase该类生成xxxJbRoomBase.java类的时候将会不处理。如果{@link JbEntity},
     * {@link JbDao}没有设置target，将会默认处理。
     */
    String setTarget() default "";

    /**
     * 数据库的名称
     * 如果不传默认将会以使用该注解类的类名作为数据库的名称
     */
    String name() default "";

    //数据库版本号
    int version();

    /**
     * 配置的值会设置在Room的<code>@Database</code>的
     * <code>views</code>中。
     */
    Class<?>[] views() default {};

    /**
     * 配置的值会设置在Room的<code>@Database</code>的
     * <code>exportSchema</code>中
     */
    boolean exportSchema() default true;

}
