package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.TestPaper;
import com.example.exception.CustomException;
import com.example.mapper.TestPaperMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 试卷信息业务层方法
 */
@Service
public class TestPaperService {

    @Resource
    private TestPaperMapper testPaperMapper;

    public void add(TestPaper testPaper) throws ParseException {
        // 先要检验前台传过来的数据
        check(testPaper);
        Account currentUser = TokenUtils.getCurrentUser();
        testPaper.setTeacherId(currentUser.getId());
        // 手动选题
        if (testPaper.getType().equals("手动选题")) {
            List<Integer> idList = testPaper.getIdList();
            testPaper.setQuestionIds(JSONUtil.toJsonStr(idList));
        }
        testPaperMapper.insert(testPaper);
    }

    public void check(TestPaper testPaper) throws ParseException {
        if (ObjectUtil.isEmpty(testPaper.getName())
                || ObjectUtil.isEmpty(testPaper.getCourseId())
                || ObjectUtil.isEmpty(testPaper.getStart())
                || ObjectUtil.isEmpty(testPaper.getEnd())
                || ObjectUtil.isEmpty(testPaper.getTime())
                || ObjectUtil.isEmpty(testPaper.getType())
        ){
            throw new CustomException("-1", "请填写完整您要提交的试卷信息");
        }
        // 开始时间要早于结束时间
        long start = new SimpleDateFormat("yyyy-MM-dd").parse(testPaper.getStart()).getTime();
        long end = new SimpleDateFormat("yyyy-MM-dd").parse(testPaper.getEnd()).getTime();
        if (start >= end) {
            throw new CustomException("-1", "开始时间必须早于结束时间");
        }
        // 校验手动选题
        if (testPaper.getType().equals("手动选题")) {
            if (CollectionUtil.isEmpty(testPaper.getIdList())) {
                throw new CustomException("-1", "手动选题方式，您需要选择具体的题目");
            }
        }
        //自动组卷校验
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

    public PageInfo<TestPaper> selectPage(TestPaper testPaper, Integer pageNum, Integer pageSize) throws ParseException {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.TEACHER.name().equals(currentUser.getRole())) {
            testPaper.setTeacherId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<TestPaper> list = testPaperMapper.selectAll(testPaper);
        // 初始化每个考试的状态
        for (TestPaper paper : list) {
            long now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(DateUtil.now()).getTime();
            long start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(paper.getStart() + " 00:00:00").getTime();
            long end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(paper.getEnd() + " 23:59:59").getTime();
            if (now < start) {
                paper.setStatus("未开始");
            }
            else if (now > end){
                paper.setStatus("已结束");
            }
            else{
                paper.setStatus("进行中");
            }
        }
        return PageInfo.of(list);
    }

}
