package com.llc.annotation;

import com.llc.scanner.ZBatisMapperScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <p>
 * ZBatisMapperScan
 * </p>
 *
 * @author llc
 * @desc 开启Zbatis功能，并且指定扫描路径
 * @since 2021-02-19 16:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ZBatisMapperScannerRegistrar.class)
public @interface ZBatisMapperScan {

    /**
     * 扫描的包名
     * @return
     */
    String[] value();
}
