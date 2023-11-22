package com.lucloud.mapper;

import com.lucloud.entity.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lucky
 * @description TODO
 * @date 2023/11/22
 */
@Mapper
public interface TestMapper {

    Test getTestById(Integer id);

    List<Test> getTestAll();

    Integer insertTest(Test test);

    Integer updateTest(Test test);

    Integer deleteTest(Integer id);
}
