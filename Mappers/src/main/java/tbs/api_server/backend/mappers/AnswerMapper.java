package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tbs.api_server.objects.simple.StandardAnswer;

@Mapper
public interface AnswerMapper
{
    @Select("SELECT * FROM answer WHERE ques_id=#{ques_id} FOR UPDATE")
    StandardAnswer getAnswerForQuestion(int ques_id);

//    @Update("UPDATE answer SET answer_content =#{answer} where ques_id=#{ques_id}")
//    int updateAnswer(int ques_id, byte[] answer);
//
//    @Update("UPDATE answer SET answer_analysis =#{analysis} where ques_id=#{ques_id}")
//    int updateAnswerAnalyst(int ques_id, byte[] analysis);

    @Insert("INSERT INTO answer(ques_id,answer_content,answer_analysis) VALUES (#{param1},#{param2},#{param3})")
    int insertAnswer(int ques_id, byte[] answer, Byte[] analysis);


    @Delete("DELETE FROM answer WHERE ques_id=#{ques_id}")
    int deleteAnswer(int ques_id);
}
