package com.llc.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;
import java.util.Set;


/**
 * <p>
 * ClassPathMapperScanner
 * </p>
 *
 * @author llc
 * @desc 路径Mapper扫描器，用于将指定路径下的类/接口声明为bean
 * @since 2021-02-19 16:28
 */
public class ClassPathMapperScanner extends ClassPathBeanDefinitionScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassPathMapperScanner.class);

    public ClassPathMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {

        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (beanDefinitionHolders.isEmpty()) {
            LOGGER.warn("No MyBatis mapper was found in '" + Arrays.toString(basePackages)
                + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitionHolders);
        }
        return beanDefinitionHolders;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            System.out.println("Creating MapperFactoryBean with name '" + holder.getBeanName() + "' and '"
                + beanClassName + "' mapperInterface");

            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is MapperFactoryBean
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(MapperFactoryBean.class);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }

    }

    /**
     * 候选条件：为独立的接口即可
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

}