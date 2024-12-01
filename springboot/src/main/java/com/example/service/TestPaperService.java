package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.TestPaper;
import com.example.mapper.TestPaperMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 试卷信息业务层方法
 */
@Service
public class TestPaperService {

    @Resource
    private TestPaperMapper testPaperMapper;

    public void add(TestPaper testPaper) {
        testPaperMapper.insert(testPaper);
    }

    public void updateById(TestPaper testPaper) {
        testPaperMapper.updateById(testPaper);
    }

    public void deleteById(Integer id) {
        testPaperMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            testPaperMapper.deleteById(id);
        }
    }

    public TestPaper selectById(Integer id) {
        return testPaperMapper.selectById(id);
    }

    public List<TestPaper> selectAll(TestPaper testPaper) {
        return testPaperMapper.selectAll(testPaper);
    }

    public PageInfo<TestPaper> selectPage(TestPaper testPaper, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<TestPaper> list = testPaperMapper.selectAll(testPaper);
        return PageInfo.of(list);
    }

}
