package com.llc.mapper;

import com.llc.annotation.Query;
import com.llc.annotation.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * MapperMethod
 * </p>
 *
 * @author llc
 * @desc 对应Mapper类里面的一个方法
 * @since 2021-02-19 16:40
 */
public class MapperMethod {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // 用于执行SQL语句
    private final JdbcTemplate jdbcTemplate;
    // SQL命令，包括语句和类型
    private final SqlCommand command;
    // 返回值类型，可以获取泛型
    private final Type rtType;
    // 返回值类型
    private final Class<?> rtClass;

    public MapperMethod(JdbcTemplate jdbcTemplate, Method method) {
        this.jdbcTemplate = jdbcTemplate;
        this.command = new SqlCommand(method);
        this.rtType = method.getGenericReturnType();
        this.rtClass = method.getReturnType();
    }

    /**
     * 执行
     *
     * @param args
     *            参数值
     * @return
     */
    public Object execute(Object[] args) {
        Object result;
        switch (command.getType()) {
            case UPDATE: {// 增/删/改
                result = jdbcTemplate.update(command.getName(), args);
                break;
            }
            case QUERY: {// 查询
                result = query(args);
                break;
            }
            default:
                throw new RuntimeException("Unknown execution method for: " + command.getName());
        }
        return result;
    }

    /**
     * 根据返回值类型调用jdbcTemplate的不同方法
     * @param args
     * @return
     */
    private Object query(Object[] args) {
        // 1、返回值类型为Map
        if (Map.class.isAssignableFrom(rtClass)) {
            return jdbcTemplate.queryForMap(command.getName(), args);
        }
        // 2、返回值类型为List
        if (List.class.isAssignableFrom(rtClass)) {
            // 2.1泛型
            if (rtType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) rtType).getActualTypeArguments();
                try {
                    Class<?> clazz = Class.forName(actualTypeArguments[0].getTypeName());
                    // 2.1.1泛型为Java自身的类
                    if (isJavaClass(clazz)) {
                        return jdbcTemplate.queryForList(command.getName(), clazz, args);
                    }
                    // 2.1.2泛型为用户自定义类
                    RowMapper<?> rm = BeanPropertyRowMapper.newInstance(clazz);
                    return jdbcTemplate.query(command.getName(), rm, args);
                } catch (ClassNotFoundException e) {
                    // 2.1.3异常时记录日志，并执行非泛型查询
                    LOG.warn("泛型转换异常！", e);
                }
            }
            // 2.2非泛型
            return jdbcTemplate.queryForList(command.getName(), args);
        }
        // 3、返回值类型为单个对象
        // 3.1Java自身的类
        if (isJavaClass(rtClass)) {
            return jdbcTemplate.queryForObject(command.getName(), rtClass, args);
        }
        // 3.2用户自定义类
        RowMapper<?> rm = BeanPropertyRowMapper.newInstance(rtClass);
        return jdbcTemplate.queryForObject(command.getName(), rm, args);
    }

    /**
     * 是否为Java自身类
     * @param clz
     * @return
     */
    private boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }

    public static class SqlCommand {
        private final String name;
        private final SqlCommandType type;

        public SqlCommand(Method method) {
            Annotation annotation;
            if ((annotation = method.getAnnotation(Query.class)) != null) {
                type = SqlCommandType.QUERY;
                name = ((Query) annotation).value();
            } else if ((annotation = method.getAnnotation(Update.class)) != null) {
                type = SqlCommandType.UPDATE;
                name = ((Update) annotation).value();
            } else {
                type = SqlCommandType.UNKNOWN;
                name = null;
            }
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }
    }

}
