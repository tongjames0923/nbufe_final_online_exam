package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import tbs.api_server.objects.simple.Question;
import tbs.api_server.objects.simple.QuestionResource;

import java.util.List;

@Mapper
public interface QuestionMapper
{
    //问题


    @Insert("INSERT INTO `question` (`que_type`, `que_creator`, `que_alter_time`, `que_file`, `publicable`, `tag`, `use_time`, `answerd`, `answerd_right`) VALUES (#{param1},#{param2}, CURRENT_TIMESTAMP, #{param3}, #{param4}, #{param5}, '0', '0', '0')")
    int insertQuestion(int que_type, int creator_id, byte[] que_file, Integer isopen, Integer tagid);

    @Select("SELECT * FROM `question` LIMIT #{from},#{num}")
    List<Question> getQuestions(int from, int num);

    @Select("SELECT * FROM `question` WHERE `tag`=#{tag} LIMIT #{from},#{num}")
    List<Question> getQuestionsByTag(int tag, int from, int num);

    @Select("select count(*) from `question`;")
    int countQuestions();

    @Delete("DELETE FROM `question` WHERE `id`=#{id}")
    int deleteQuestion(int id);

    //问题资源
    @Insert("INSERT INTO `ques_resource` ( `resource`, `note`, `resource_type`) VALUES (#{param2}, #{param3}, #{param1});\n")
    int uploadResource(int res_type, String res, String note);

    @Select("SELECT * FROM `ques_resource` WHERE `resource_type` = #{t} limit #{from},#{num}")
    List<QuestionResource> getResourcesByType(int t, int from, int num);

    @Select("SELECT * FROM `ques_resource` WHERE `note` LIKE %${note}% LIMIT #{from},#{num}")
    List<QuestionResource> findResourcesByNote(String note, int from, int num);

    @Select("SELECT * FROM `ques_resource` WHERE `id` = #{id}")
    QuestionResource getResourceById(int id);


    @Select("SELECT COUNT(*) FROM `ques_resource` WHERE `resource` = #{res} ")
    int containsResource(String res);

    @Delete("DELETE FROM `ques_resource` WHERE `id`= #{id}")
    int deleteResourceById(int id);

    @Update("UPDATE `ques_resource` SET `note` = #{note} WHERE `id` = #{id}")
    int updateResourceNoteById(int id, String note);

    //资源链接
    @Insert("INSERT INTO `resource_link` ( `ques_id`, `resource_id`) VALUES (#{param1},#{param2});\n")
    int linkResource(int ques_id, int res_id);

    @Delete("DELETE FROM `resource_link` WHERE `resource_id`=#{param2} and `ques_id` = #{param1}")
    int unlinkResource(int ques_id, int resource_id);


}
