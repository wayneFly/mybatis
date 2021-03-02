package com.llc.mapper;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * <p>
 * MapperProxy
 * </p>
 *
 * @author llc
 * @desc Mapper代理的InvocationHandler
 * @since 2021-02-19 16:39
 */
public class MapperProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = -6424540398559729838L;

    private final JdbcTemplate jdbcTemplate;
    private final Map<Method, MapperMethod> methodCache;

    public MapperProxy(JdbcTemplate jdbcTemplate, Map<Method, MapperMethod> methodCache) {
        this.jdbcTemplate = jdbcTemplate;
        this.methodCache = methodCache;
    }

    /**
     * 方法调用
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, args);
            } else if (isDefaultMethod(method)) {
                return invokeDefaultMethod(proxy, method, args);
            }
        } catch (Throwable t) {
            throw t;
        }
        final MapperMethod mapperMethod = cachedMapperMethod(method);
        return mapperMethod.execute(args);
    }

    private MapperMethod cachedMapperMethod(Method method) {
        return methodCache.computeIfAbsent(method, k -> new MapperMethod(jdbcTemplate, method));
    }

    /**
     * 调用默认方法
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
            .getDeclaredConstructor(Class.class, int.class);
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        final Class<?> declaringClass = method.getDeclaringClass();
        return constructor
            .newInstance(declaringClass,
                MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED | MethodHandles.Lookup.PACKAGE
                    | MethodHandles.Lookup.PUBLIC)
            .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
    }

    /**
     * 是否为默认方法
     *
     * @param method
     * @return
     */
    private boolean isDefaultMethod(Method method) {
        return (method.getModifiers() & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
            && method.getDeclaringClass().isInterface();
    }

}
