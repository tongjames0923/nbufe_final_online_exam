package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import tbs.api_server.objects.simple.Question;

import java.util.List;

@Mapper
public interface QuestionMapper
{
    //问题


    @Insert("INSERT INTO `question` (`que_type`, `que_creator`, `que_alter_time`, `que_file`, `publicable`, `tag`, `use_time`, `answerd`, `answerd_right`,`title`) VALUES (#{que_type},#{creator_id}, CURRENT_TIMESTAMP, #{que_file}, #{isopen}, #{tagid}, '0', '0', '0',#{title})")
    int insertQuestion(int que_type, int creator_id, byte[] que_file,String title, Integer isopen, Integer tagid);

    @Select("SELECT `que_id`,`que_type`, `que_creator`, `que_alter_time`, `publicable`, `tag`, `use_time`, `answerd`, `answerd_right`,`title` FROM `question` LIMIT #{from},#{num} FOR UPDATE")
    List<Question> getQuestions(int from, int num);

    @Select("SELECT `que_id`,`que_type`, `que_creator`, `que_alter_time`, `publicable`, `tag`, `use_time`, `answerd`, `answerd_right`,`title` FROM `question` WHERE `tag`=#{tag} LIMIT #{from},#{num} FOR UPDATE")
    List<Question> getQuestionsByTag(int tag, int from, int num);

    @Select("SELECT `que_id`,`que_type`, `que_creator`, `que_alter_time`, `publicable`, `tag`, `use_time`, `answerd`, `answerd_right`,`title` FROM `question` WHERE `que_type`=#{type} LIMIT #{from},#{num} FOR UPDATE")
    List<Question> getQuestionsByType(int type, int from, int num);

    @Select("SELECT `que_id`,`que_type`, `que_creator`, `que_alter_time`, `publicable`, `tag`, `use_time`, `answerd`, `answerd_right`,`title` " +
            "FROM `question` WHERE `title` LIKE %${title}% LIMIT #{from},#{num} FOR UPDATE")
    List<Question> findQuestionByTitle(String title,int from,int num);

    @Select("SELECT `que_file` FROM `question` WHERE `que_id`=#{quesid} FOR UPDATE")
    byte[] getQuestionFile(int quesid);

    @Update("UPDATE `que_id`,`question` SET `${field}`=#{value} WHERE `que_id`=#{id}")
    int updateQuestionValue(int que_id,String field,Object value);

    @Select("select count(*) from `question` FOR UPDATE")
    int countQuestions();

    @Delete("DELETE FROM `question` WHERE `id`=#{que_id}")
    int deleteQuestion(int id);


    @Select("SELECT `que_id`,`que_type`, `que_creator`, `que_alter_time`, `publicable`, `tag`, `use_time`, `answerd`, `answerd_right`,`title` " +
            "FROM `question` WHERE `que_id`=#{que_id} and `que_creator`=#{user} FOR UPDATE")
    Question OwnQuestion(int que_id, int user);

}
