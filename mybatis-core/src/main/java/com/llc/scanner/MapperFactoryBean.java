package com.llc.scanner;

import com.llc.mapper.MapperRegisty;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * MapperFactoryBean
 * </p>
 *
 * @author llc
 * @desc
 * @since 2021-02-19 16:33
 */
public class MapperFactoryBean<T> implements FactoryBean<T>, InitializingBean {

    private Class<T> mapperInterface;

    public MapperFactoryBean() {
    }

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Autowired
    private MapperRegisty mapperRegisty;

    @Override
    public T getObject() throws Exception {
        return mapperRegisty.getMapper(mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return this.mapperInterface;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mapperRegisty.addMapper(mapperInterface);
    }
}
