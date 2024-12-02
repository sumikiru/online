package com.example.mapper;

import com.example.entity.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface QuestionMapper {

    int insert(Question question);

    void updateById(Question question);

    void deleteById(Integer id);

    @Select("select question.*,question_type.score as typeScore , question_type.name as typeName from `question` " +
            "left join question_type on question.type_id=question_type.id" +
            " where id = #{id}")
    Question selectById(Integer id);

    List<Question> selectAll(Question question);

    @Select("select * from `question` where course_id = #{course_id} and type_id = #{type_id}")
    List<Question> selectByCourseIdAndTypeId(@Param("courseId") Integer courseId, @Param("typeId") Integer typeId);
}
