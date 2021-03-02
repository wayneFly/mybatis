package com.llc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * Update
 * </p>
 *
 * @author llc
 * @desc  操作类（增加/修改/删除）语句
 * @since 2021-02-19 16:42
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Update {

    String value();
}
