package com.llc.service;

import com.llc.annotation.Query;
import com.llc.annotation.Update;
import com.llc.entity.User;
import com.llc.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author llc
 * @desc
 * @since 2021-02-19 17:00
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User selectById(Integer id){
        return userMapper.selectById(id);
    }

    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    public int countUser() {
        return userMapper.countUser();
    }

    public int insert(int id,String name,Integer age){
        return userMapper.insert(id,name,age);
    }

    public List<String> selectAllName() {
        return userMapper.selectAllName();
    }
}
