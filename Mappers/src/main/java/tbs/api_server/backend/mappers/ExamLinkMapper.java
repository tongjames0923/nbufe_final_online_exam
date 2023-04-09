package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import tbs.api_server.objects.compound.exam.ExamQuestion;
import tbs.api_server.objects.simple.ExamQuestionLink;

import java.util.List;

@Mapper
public interface ExamLinkMapper {
    @Insert("insert into `exam_link`(questionid,examname,score,insertor) values (" +
            "#{qid},#{examname},#{score},#{insertor}" +
            ");")
    int insertQuestionLink(int qid, String examname, int score, String insertor);

    @Select("select *  from exam_link where examname=#{name}")
    @Cacheable(value = "ques_links",key = "#name")
    List<ExamQuestionLink> getExamQuestions(String name);

    @Select("select score from exam_link where examname=#{ename} and questionid=#{q}")
    double score(String ename,int q);

    @Update("update `exam_link` set score=#{score} where questionid=#{q} and examname=#{e}")
    @CacheEvict(value = "ques_links",key = "#e")
    int updateScoreByQandE(int q,String e,int score);

}
