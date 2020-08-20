package com.hmz.roomhelper_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***********************************************************
 * 创建时间:2020-06-21
 * 作   者: [hanmingze]
 * describe: 用于生成Room的@Database修饰的抽象类
 * 备注信息: {在类似加上该标记，在编译期间会生产/app/build/generated
 * /ap_generated_sources/debug/out/标记类类路径/xxx_Hlp.java
 * 文件。生成个该文件是对Room的<code>@Database</code>的标记使用。启动会生产
 * 一个<code>roomInit()</code>方法该方法会返回一个<code>HelperBuilder</code>
 * 可用于继续配置数据库}
 **********************************************************/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface DatabaseHlp {
    /**
     * 该标记会和<code>target</code>的标签进行匹配。
     * 如果{@link EntityHlp},{@link DaoHlp}注解中的填写的<code>target</code>
     * 和该标记的值一样，那么在自动生成xxx_Hlp的时候会将@link EntityHlp},{@link DaoHlp}
     * 标记的类添加到<code>@Database</code>的entities集合中，或者在xxx_Hlp中自动
     * 生成getxxxDao()的方法。
     *
     * @return 如果设置改值，但是与{@link EntityHlp},{@link DaoHlp}设置的target不匹配，那么
     * 以标记{@link DatabaseHlp}的类在生成xxx_Hlp.java类的时候将会不处理不一致的{@link EntityHlp}和{@link DaoHlp}标记的类。
     * 如果{@link EntityHlp},{@link DaoHlp}没有设置target，将会默认处理。
     */
    String setTarget() default "";

    /**
     * 数据库的名称
     * 如果不传默认将会以使用该注解类的类名作为数据库的名称
     *
     * @return 默认""
     */
    String name() default "";
    /**
     * 数据库版本号
     *
     * @return 可以不用指定，框架默认设置为1。在不指定情况下，框架会根据自定生成的Migrate来决定版本号
     * 会从@EntityHlp，或者@FieldHlp 注解中的endVersion中取一个最大值作为版本号
     */
    int version() default 0;

    /**
     * 配置的值会设置在Room的<code>@Database</code>的
     * <code>views</code>中。
     *
     * @return 默认{}
     */
    Class<?>[] views() default {};

    /**
     * 配置的值会设置在Room的<code>@Database</code>的
     * <code>exportSchema</code>中
     *
     * @return 默认true
     */
    boolean exportSchema() default true;

}
