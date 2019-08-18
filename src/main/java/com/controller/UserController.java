package com.controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dao.UserDao;
import com.domain.User;
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    UserDao userDao;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @RequestMapping("/list")
    public List<User> listAllUSer(){

        return userDao.listAllUSer();
    }

    @RequestMapping("/testRedis")
    public String testRedis(){
        //1从redis缓存中获取
        String userJson = redisTemplate.boundValueOps("user.findAll").get();
        if(userJson == null){
            //2. 缓存没有从数据库中查询
            List<User> users = userDao.listAllUSer();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                userJson = objectMapper.writeValueAsString(users);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //3. 存入缓存
            redisTemplate.boundValueOps("user.findAll").set(userJson);

            System.out.println("从mysql中获取的user");
        }else{
            System.out.println("从redis中获取的user");
        }
        return userJson;
    }
}
