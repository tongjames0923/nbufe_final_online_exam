package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tbs.api_server.objects.simple.ExamInfo;

import java.util.List;

@Mapper
public interface ExamMapper
{
    @Select("Select `exam_id`,`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_status` from `exam` where `status`=#{status} limit #{from},#{num")
    List<ExamInfo> getExamsByStatus(int status,int from,int num);

    @Select("SELECT `exam_file` FROM `exam` where `exam_id`=#{exam_id}")
    byte[] getExamFile(int exam_id);

    @Delete("DELETE FROM `exam` WHERE `exam_id`=#{exam_id}")
    int deleteExam(int exam_id);

    @Update("UPDATE `exam` SET `exam_status`=#{status} WHERE `exam_id`=#{exam_id}")
    int updateExamStatus(int exam_id,int status);

}
