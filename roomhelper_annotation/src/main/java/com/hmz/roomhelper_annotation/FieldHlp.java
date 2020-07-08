package com.hmz.roomhelper_annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***********************************************************
 * 创建时间:2020-06-23
 * 作   者: [hanmingze]
 * describe: 该标签可以对已有的表中的字段进行增、删、改。
 * 备注信息: {增: 当该注解标注在一个属性上，并且该注解oldFieldName什么值也没有设置的时候
 *           改: 当该注解修饰在一个属性上，并且有设置该注解的{@link #oldFieldName()}值 }
 **********************************************************/
@Target({ElementType.FIELD,
        ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface FieldHlp {

    /**
     * 删除表中的某个字段,如果删除字段，那么请教@FeildHlp注解用在类似标记
     *
     * @return 删除的字段名
     */
    String[] deleFieldName() default "";

    /**
     * 修改从哪个版本号开始
     *
     * @return 如果为0 则会以当前版本号为开始本版
     */
    int oldStartVersion() default 0;

    /**
     * 修改以哪个版本结束
     *
     * @return 如果为0 则会以当前版本号+1作为结束版本号
     */
    int oldEndVersion() default 0;

    /**
     * 是否可以为空， 默认情况下可以为空，但是int的各类型不能为空
     *
     * @return 如果true 则为不为空，那么就会获取 标记的字段值
     */
    boolean notNull() default false;

    /**
     * 设置该值，代表要对已有的表字段进行修改
     * 里面添加的值为修改前的字段名
     *
     * @return 修改前的字段名
     */
    String oldFieldName() default "";

    /**
     * 修改从哪个版本号开始
     *
     * @return 如果为0 则会以当前版本号为开始本版
     */
    int startVersion();

    /**
     * 修改以哪个版本结束
     *
     * @return 如果为0 则会以当前版本号+1作为结束版本号
     */
    int endVersion();
}
