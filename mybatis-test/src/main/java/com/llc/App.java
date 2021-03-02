package com.llc;

import com.llc.annotation.ZBatisMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * App
 * </p>
 *
 * @author llc
 * @desc
 * @since 2021-02-19 16:46
 */
@SpringBootApplication
@ZBatisMapperScan("com.llc.mapper")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
