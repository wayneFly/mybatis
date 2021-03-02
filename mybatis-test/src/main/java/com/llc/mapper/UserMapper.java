package com.llc.mapper;

import com.llc.annotation.Query;
import com.llc.annotation.Update;
import com.llc.entity.User;

import java.util.List;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author llc
 * @desc
 * @since 2021-02-19 16:57
 */
public interface UserMapper {

    @Query("select * from user where id = ?")
    User selectById(Integer id);

    @Query("select * from user")
    List<User> selectAll();

    @Query("select count(*) from user")
    int countUser();

    @Update("insert into user values (?,?,?)")
    int insert(int id,String name,Integer age);

    @Query("select name from user")
    List<String> selectAllName();

}
