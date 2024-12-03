package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.ScoreMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务层方法
 */
@Service
public class ScoreService {

    @Resource
    private ScoreMapper scoreMapper;

    public void add(TestPaper testPaper) {

        List<Answer> list = new ArrayList<>();
        for (Question question : testPaper.getQuestions()) {
            Answer answer = new Answer();
            answer.setTypeName(question.getTypeName());
            answer.setScore(question.getTypeScore());
            answer.setQuestionId(question.getId());
            if ("多选题".equals(question.getTypeName())) {
                List<String> checkList = question.getCheckList();
                answer.setNewAnswer(String.join(",", checkList));
            } else {
                answer.setNewAnswer(question.getNewAnswer());
            }
            answer.setAnswer(question.getAnswer());
            list.add(answer);
        }

        Score score = new Score();
        Account currentUser = TokenUtils.getCurrentUser();
        score.setStudentId(currentUser.getId());
        score.setTeacherId(currentUser.getId());
        score.setName(testPaper.getName());
        score.setPaperId(testPaper.getId());
        score.setStatus("待阅卷");
        score.setAnswer(JSONUtil.toJsonStr(list));
        scoreMapper.insert(score);
    }

    public void updateById(Score score) {
        scoreMapper.updateById(score);
    }

    public void deleteById(Integer id) {
        scoreMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            scoreMapper.deleteById(id);
        }
    }

    public Score selectById(Integer id) {
        return scoreMapper.selectById(id);
    }

    public List<Score> selectAll(Score score) {
        return scoreMapper.selectAll(score);
    }

    public PageInfo<Score> selectPage(Score score, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.STUDENT.name().equals(currentUser.getRole())) {
            score.setStudentId(currentUser.getId());
        }
        if (RoleEnum.TEACHER.name().equals(currentUser.getRole())) {
            score.setTeacherId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Score> list = scoreMapper.selectAll(score);
        return PageInfo.of(list);
    }

}
