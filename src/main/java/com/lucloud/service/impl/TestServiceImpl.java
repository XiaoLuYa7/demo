package com.lucloud.service.impl;

import com.lucloud.entity.Test;
import com.lucloud.mapper.TestMapper;
import com.lucloud.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lucky
 * @description TODO
 * @date 2023/11/22
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    //获取Test
    @Override
    public Test getTestById(Integer id) {
        return testMapper.getTestById(id);
    }

    //获取所有Test
    @Override
    public List<Test> getTestAll() {
        return testMapper.getTestAll();
    }

    //插入Test
    @Override
    public boolean insertTest(Test test) {
        return testMapper.insertTest(test) > 0;
    }

    //更新Test
    @Override
    public boolean updateTest(Test test) {
        return testMapper.updateTest(test) > 0;
    }

    //删除Test
    @Override
    public boolean deleteTest(Integer id) {
        return testMapper.deleteTest(id) > 0;
    }
}
