package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.CourseMapper;
import com.example.mapper.QuestionMapper;
import com.example.mapper.TeacherMapper;
import com.example.mapper.TestPaperMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 试卷信息业务层方法
 */
@Service
public class TestPaperService {

    @Resource
    private TestPaperMapper testPaperMapper;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private CourseMapper courseMapper;
    @Resource
    private TeacherMapper teacherMapper;

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
        // 自动组卷
        else if (testPaper.getType().equals("自动组卷")) {
            List<Integer> list = new ArrayList<>();
            // 自动选取每个题型的题目
            randomQuestionIds(testPaper, testPaper.getChoiceNum(), list, 1, "单选题");
            randomQuestionIds(testPaper, testPaper.getMultiChoiceNum(), list, 2, "多选题");
            randomQuestionIds(testPaper, testPaper.getCheckNum(), list, 3, "判断题");
            randomQuestionIds(testPaper, testPaper.getFillInNum(), list, 4, "填空题");
            randomQuestionIds(testPaper, testPaper.getAnswerNum(), list, 5, "简答题");

            testPaper.setQuestionIds(JSONUtil.toJsonStr(list));
        }
        testPaperMapper.insert(testPaper);
    }

    private void randomQuestionIds(TestPaper testPaper, Integer choiceNum, List<Integer> list, Integer typeId, String typeName) {
        List<Question> choiceList = questionMapper.selectByCourseIdAndTypeId(testPaper.getCourseId(), typeId);
        if (choiceList.size() < choiceNum){
            throw new CustomException("-1", "题库中" + typeName + "数量不足，请减少出题的" + typeName + "数量或者增加题库中的" + typeName + "数量");
        }
        Collections.shuffle(choiceList);    //打乱
        List<Integer> questions = choiceList.subList(0, choiceNum).stream().map(Question::getId).toList();
        list.addAll(questions);
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
        if (testPaper.getType().equals("自动组卷")) {
            if (ObjectUtil.isEmpty(testPaper.getChoiceNum())
                    || ObjectUtil.isEmpty(testPaper.getMultiChoiceNum())
                    || ObjectUtil.isEmpty(testPaper.getFillInNum())
                    || ObjectUtil.isEmpty(testPaper.getCheckNum())
                    || ObjectUtil.isEmpty(testPaper.getAnswerNum())
            ){
                throw new CustomException("-1", "请填写题型的数量，如果不需要某个题型，请输入0");
            }
            if (testPaper.getChoiceNum() < 0
                    || testPaper.getMultiChoiceNum() < 0
                    || testPaper.getFillInNum() < 0
                    || testPaper.getCheckNum() < 0
                    || testPaper.getAnswerNum() < 0
            ) {
                throw new CustomException("-1", "题型的数量不能小于0");
            }
        }
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
        TestPaper testPaper=testPaperMapper.selectById(id);
        Course course = courseMapper.selectById(testPaper.getCourseId());
        if (ObjectUtil.isNotEmpty(course)) {
            testPaper.setCourseName(course.getName());
        }
        Teacher teacher = teacherMapper.selectById(testPaper.getTeacherId());
        if (ObjectUtil.isNotEmpty(teacher)) {
            testPaper.setTeacherName(teacher.getUsername());
        }
        String questionIds = testPaper.getQuestionIds();
        List<Integer> idList = JSONUtil.toList(questionIds, Integer.class);
        List<Question> questions = new ArrayList<>();
        for(Integer questionId : idList){
            Question question = questionMapper.selectById(questionId);
            questions.add(question);

        }
        testPaper.setQuestions(questions);
        return testPaper;
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
