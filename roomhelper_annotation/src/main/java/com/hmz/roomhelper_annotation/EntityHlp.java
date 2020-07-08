package com.hmz.roomhelper_annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***********************************************************
 * 创建时间:2020-06-21
 * 作   者: [hanmingze]
 * describe: 用于正在生产Room的<code>@Database</code>标记类的时候
 * 作为<code>entities</code>的值
 * 备注信息: {该标签必须配合Room的@Entity使用，不然Room在无法生产相关数据库信息}
 **********************************************************/
@Target({ElementType.TYPE,
        ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface EntityHlp {

    /**
     * 使用该标记可以指定在可以在那个{@link DatabaseHlp}标记类生成xxx_Hlp.java
     * 的时候处理该标签
     *
     * @return 默认返回""，代表所有{@link DatabaseHlp}注解自动生成xxx_Hlp.java类
     * 的时候都可以处理该标签
     */
    String target() default "";

    /**
     * 修改表名
     * @return 旧的表名，在修改表名的时候会以Room的@Entity tablename中的名称为最新表名
     */
    String oldTableName()default "";

    /**
     * 添加表的标记
     *
     * @return true 代表 该表是要添加的表
     */
    boolean addTable() default false;

    /**
     * 删除某个表
     * 如果想删除某个表，一定要将@EntityHlp注解用在类的属性上，
     * 并且该数据的类型一定要是String类型，且属性的只就是要删除的表名称
     * 例如：@EntityHlp(startVersion = 1, endVersion = 2)
     * String deletTestEnitiy2 = "tb_testEntity2";
     * 尽量在@Database标记的类中属性中使用该注解
     *
     * @return true代表删除
     */
    boolean deletTable() default false;

    /**
     * 修改从哪个版本号开始
     *
     * @return 如果为0 则会以当前版本号为开始本版 -1代表无效
     */
    int startVersion() default -1;

    /**
     * 修改以哪个版本结束
     *
     * @return 如果为0 则会以当前版本号+1作为结束版本号
     */
    int endVersion() default -1;
}
