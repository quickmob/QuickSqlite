package com.lookballs.sqlite.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * 为列设置可为空的约束
     */
    boolean nullable() default true;

    /**
     * 为列设置唯一约束
     */
    boolean unique() default false;

    /**
     * 不管列类型是什么，都为列设置String类型的默认值
     */
    String defaultValue() default "";

    /**
     * 字段描述
     */
    String desc() default "";

    /**
     * 忽略将此字段映射到列中
     */
    boolean ignore() default false;
}
