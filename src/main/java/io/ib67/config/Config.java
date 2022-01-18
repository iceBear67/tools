package io.ib67.config;

import java.lang.annotation.*;

/**
 * Config class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE})
public @interface Config {
    String value() default "config.json";

    boolean staticMode() default true;
}
