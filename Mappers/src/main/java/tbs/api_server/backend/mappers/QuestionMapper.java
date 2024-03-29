package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import tbs.api_server.objects.simple.Question;

import java.util.List;

@Mapper
public interface QuestionMapper {
    //问题
    @Insert("INSERT INTO `question` (`que_id`,`que_type`, `que_creator`, `que_alter_time`, `que_file`, `publicable`,   `answerd`, `answerd_right`,`title`) VALUES (#{id},#{que_type},#{creator_id}, CURRENT_TIMESTAMP, #{que_file}, #{isopen},'0', '0',#{title})")
    int insertQuestion(long id, int que_type, int creator_id, String que_file, String title, Integer isopen);

    @Select("SELECT q.`que_id`,q.`que_type`, q.`que_creator`,ans.answer_content as answer_data, q.`que_alter_time`, q.`publicable`,(select count(1) from exam_link where questionid=q.que_id) as use_time, q.`answerd`, q.`answerd_right`,q.`title` " +
            "FROM `question` q join `answer` ans on ans.ques_id=q.que_id " +
            "LIMIT #{from},#{num} FOR UPDATE")
    List<Question> getQuestions(int from, int num);

    @Select("SELECT q.`que_id`,q.`que_type`, q.`que_creator`,ans.answer_content as answer_data, q.`que_alter_time`, q.`publicable`,(select count(1) from exam_link where questionid=q.que_id) as use_time, q.`answerd`, q.`answerd_right`,q.`title` " +
            "FROM `question` q join `answer` ans on ans.ques_id=q.que_id " +
            "WHERE `que_type`=#{type} LIMIT #{from},#{num} FOR UPDATE")
    List<Question> getQuestionsByType(int type, int from, int num);

    @Select("SELECT q.`que_id`,q.`que_type`, q.`que_creator`,ans.answer_content as answer_data, q.`que_alter_time`, q.`publicable`,(select count(1) from exam_link where questionid=q.que_id) as use_time, q.`answerd`, q.`answerd_right`,q.`title` " +
            "FROM `question` q join `answer` ans on ans.ques_id=q.que_id " +
            "WHERE `title` LIKE '%${title}%' LIMIT #{from},#{num} FOR UPDATE")
    List<Question> findQuestionByTitle(String title, int from, int num);

    @Select("SELECT `que_file` FROM `question` WHERE `que_id`=#{quesid} FOR UPDATE")
    @Cacheable(value = "queFile", key = "#quesid")
    String getQuestionFile(int quesid);

    @Update("UPDATE `question` SET `${field}`=#{value} WHERE `que_id`=#{id}")
    @Caching(evict = {
            @CacheEvict(value = "queFile", key = "#id"),
            @CacheEvict(value = "que", key = "#id")
    })
    int updateQuestionValue(int id, String field, Object value);

    @Update("update exam_db.question set exam_db.question.answerd=exam_db.question.answerd+#{answerd},exam_db.question.answerd_right=exam_db.question.answerd_right+#{righted} where exam_db.question.que_id=#{id}")
    @CacheEvict(value = "que", key = "#id")
    int updateAnswerStatus(int id,int answerd,int righted);

    @Select("select count(*) from `question` where publicable=1 or que_creator=#{user} or #{level}>=2")
    int countQuestions(int user,int level);

    @Delete("DELETE FROM `question` WHERE `que_id`=#{id}")
    @Caching(evict = {
            @CacheEvict(value = "queFile", key = "#id"),
            @CacheEvict(value = "que", key = "#id")
    })
    int deleteQuestion(int id);

    @Select("SELECT q.`que_id`,q.`que_type`, q.`que_creator`,ans.answer_content as answer_data, q.`que_alter_time`, q.`publicable`,(select count(1) from exam_link where questionid=q.que_id) as use_time, q.`answerd`, q.`answerd_right`,q.`title` " +
            "FROM `question` q join `answer` ans on ans.ques_id=q.que_id " +
            " WHERE `que_id`=#{que_id} FOR UPDATE")
    @Cacheable(value = "que", key = "#id")
    Question getQuestionByID(int id);

    @Select("SELECT q.`que_id`,q.`que_type`, q.`que_creator`,ans.answer_content as answer_data, q.`que_alter_time`, q.`publicable`,(select count(1) from exam_link where questionid=q.que_id) as use_time, q.`answerd`, q.`answerd_right`,q.`title` " +
            "FROM `question` q join `answer` ans on ans.ques_id=q.que_id " +
            "WHERE `que_id`=#{que_id} and `que_creator`=#{user} FOR UPDATE")
    Question OwnQuestion(int que_id, int user);

    @Select("SELECT distinct q.`que_id`,q.`que_type`, q.`que_creator`,ans.answer_content as answer_data, q.`que_alter_time`, q.`publicable`,(select count(1) from exam_link where questionid=q.que_id) as use_time, q.`answerd`, q.`answerd_right`,q.`title` " +
            "FROM `question` q join `answer` ans on ans.ques_id=q.que_id " +
            " JOIN tag_link l ON l.ques_id=q.que_id JOIN tag t ON t.tag_id=l.tag_id WHERE t.tag_name LIKE '${tag}%'")
    List<Question> getQuestionByTag(@Param(value = "tag") String tag);

    @Select("select el.questionid from exam_link el where examname=#{examname}")
    List<Integer> listQuestionIdByExam(String examname);

}
