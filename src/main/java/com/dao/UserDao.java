package com.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.domain.User;

public interface UserDao {
    @Select("select * from user")
    public List<User> listAllUSer();
}
