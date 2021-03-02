package com.llc.scanner;

import com.llc.annotation.ZBatisMapperScan;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * ZBatisMapperScannerRegistrar
 * </p>
 *
 * @author llc
 * @desc 对应ZBatisMapperScan注解的@Import
 * @since 2021-02-19 16:17
 */
public class ZBatisMapperScannerRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        AnnotationAttributes annotationAttributes =
            AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(ZBatisMapperScan.class.getName()));

        if(annotationAttributes != null ) {
            registerBeanDefinitions(annotationAttributes,beanDefinitionRegistry);
        }
    }

    void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText)
            .collect(Collectors.toList()));
        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

}
