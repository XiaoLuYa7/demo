package com.lucloud.service;

import com.lucloud.entity.Test;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TestService {
    //获取Test
    Test getTestById(Integer id);

    //获取所有Test
    List<Test> getTestAll();

    //插入Test
    boolean insertTest(Test test);

    //更新Test
    boolean updateTest(Test test);

    //删除Test
    boolean deleteTest(Integer id);
}
