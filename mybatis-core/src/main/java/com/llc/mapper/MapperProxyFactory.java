package com.llc.mapper;

import org.springframework.cglib.proxy.Proxy;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * MapperProxyFactory
 * </p>
 *
 * @author llc
 * @desc Mapper代理工厂，每一个Mapper对应此类的一个实例
 * @since 2021-02-19 16:38
 */
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;
    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<>();

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public Map<Method, MapperMethod> getMethodCache() {
        return methodCache;
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface },
            mapperProxy);
    }

    public T newInstance(JdbcTemplate jdbcTemplate) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(jdbcTemplate, methodCache);
        return newInstance(mapperProxy);
    }

}
