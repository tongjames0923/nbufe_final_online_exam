package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import tbs.api_server.objects.simple.StandardAnswer;

import java.util.List;

@Mapper
public interface AnswerMapper
{
    @Select("SELECT a.*,q.que_type as type FROM answer a join question q on q.que_id=a.ques_id WHERE a.ques_id=#{ques_id} FOR UPDATE")
    StandardAnswer getAnswerForQuestion(int ques_id);

//    @Update("UPDATE answer SET answer_content =#{answer} where ques_id=#{ques_id}")
//    int updateAnswer(int ques_id, byte[] answer);
//
//    @Update("UPDATE answer SET answer_analysis =#{analysis} where ques_id=#{ques_id}")
//    int updateAnswerAnalyst(int ques_id, byte[] analysis);

    @Insert("INSERT INTO answer(ques_id,answer_content,answer_analysis) VALUES (#{param1},#{param2},#{param3})")
    int insertAnswer(int ques_id,String answer, String analysis);


    @Select("SELECT a.*,q.que_type AS type FROM question q JOIN answer a ON q.que_id=a.ques_id AND q.que_id WHERE q.que_id IN #{qids}")
    List<StandardAnswer> listAnswerByQuestions(List<Integer> qids);

    @Delete("DELETE FROM answer WHERE ques_id=#{ques_id}")
    int deleteAnswer(int ques_id);
}
