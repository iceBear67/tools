package io.ib67.util.annotations;

/**
 * 标注一个内部 API
 */
public @interface Internal {
    String value() default "";
}
