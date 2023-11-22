package com.lucloud.controller;

import com.lucloud.entity.Test;
import com.lucloud.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lucky
 * @description TODO
 * @date 2023/11/22
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testServiceImpl;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    //获取Test
    @GetMapping("/{id}")
    @ResponseBody
    public Test getTestById(@PathVariable("id") Integer id) {
        redisTemplate.opsForValue().set("key","value123");
        return testServiceImpl.getTestById(id);
    }

    //获取所有Test
    @GetMapping("/all")
    List<Test> getTestAll() {
        return testServiceImpl.getTestAll();
    }

    //插入Test
    @PostMapping("/add")
    public boolean insertTest(Test test) {
        return testServiceImpl.insertTest(test);
    }

    //更新Test
    @PutMapping("/update")
    public boolean updateTest(Test test) {
        return testServiceImpl.updateTest(test);
    }

    //删除Test
    @DeleteMapping("/{id}")
    public boolean deleteTest(@PathVariable("id") Integer id) {
        return testServiceImpl.deleteTest(id);
    }
}
