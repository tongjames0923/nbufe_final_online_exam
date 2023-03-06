package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import tbs.api_server.objects.simple.Question;

import java.util.List;

@Mapper
public interface QuestionMapper
{
    //问题


    @Insert("INSERT INTO `question` (`que_type`, `que_creator`, `que_alter_time`, `que_file`, `publicable`,  `use_time`, `answerd`, `answerd_right`,`title`,`answer_data`) VALUES (#{que_type},#{creator_id}, CURRENT_TIMESTAMP, #{que_file}, #{isopen}, '0', '0', '0',#{title},#{ans})")
    int insertQuestion(int que_type, int creator_id, byte[] que_file,String title, Integer isopen,String ans);

    @Select("SELECT `que_id`,`que_type`, `que_creator`,`answer_data`, `que_alter_time`, `publicable`,`use_time`, `answerd`, `answerd_right`,`title` FROM `question` LIMIT #{from},#{num} FOR UPDATE")
    List<Question> getQuestions(int from, int num);

    @Select("SELECT `que_id`,`que_type`, `que_creator`,`answer_data`, `que_alter_time`, `publicable`, `use_time`, `answerd`, `answerd_right`,`title` FROM `question` WHERE `que_type`=#{type} LIMIT #{from},#{num} FOR UPDATE")
    List<Question> getQuestionsByType(int type, int from, int num);

    @Select("SELECT `que_id`,`que_type`, `que_creator`,`answer_data`, `que_alter_time`, `publicable`,`use_time`, `answerd`, `answerd_right`,`title` " +
            "FROM `question` WHERE `title` LIKE '%${title}%' LIMIT #{from},#{num} FOR UPDATE")
    List<Question> findQuestionByTitle(String title,int from,int num);

    @Select("SELECT `que_file` FROM `question` WHERE `que_id`=#{quesid} FOR UPDATE")
    @Cacheable(value = "queFile",key = "#quesid")
    Question getQuestionFile(int quesid);

    @Update("UPDATE `question` SET `${field}`=#{value} WHERE `que_id`=#{id}")
@Caching(evict = {
        @CacheEvict(value = "queFile",key = "#id"),
        @CacheEvict(value = "que",key = "#id")
})
    int updateQuestionValue(int id,String field,Object value);

    @Select("select count(*) from `question` FOR UPDATE")
    int countQuestions();

    @Delete("DELETE FROM `question` WHERE `que_id`=#{id}")
    @Caching(evict = {
            @CacheEvict(value = "queFile",key = "#id"),
            @CacheEvict(value = "que",key = "#id")
    })
    int deleteQuestion(int id);

    @Select("SELECT `que_id`,`que_type`,`answer_data`, `que_creator`, `que_alter_time`, `publicable`, `use_time`, `answerd`, `answerd_right`,`title` " +
            "FROM `question` WHERE `que_id`=#{que_id} FOR UPDATE")
    @Cacheable(value = "que",key = "#id")
    Question getQuestionByID(int id);

    @Select("SELECT `que_id`,`que_type`,`answer_data`, `que_creator`, `que_alter_time`, `publicable`, `use_time`, `answerd`, `answerd_right`,`title` " +
            "FROM `question` WHERE `que_id`=#{que_id} and `que_creator`=#{user} FOR UPDATE")
    Question OwnQuestion(int que_id, int user);

    @Select("SELECT q.* FROM question q JOIN tag_link l ON l.ques_id=q.que_id JOIN tag t ON t.tag_id=l.tag_id WHERE t.tag_name LIKE '%${tag}%'")
    List<Question> getQuestionByTag(@Param(value = "tag") String tag);
}
