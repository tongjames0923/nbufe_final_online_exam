package tbs.api_server.backend.mappers;

import org.apache.ibatis.annotations.*;
import tbs.api_server.objects.simple.ExamInfo;

import java.util.Date;
import java.util.List;

@Mapper
public interface ExamMapper
{
    @Select("Select `exam_id`,`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_status` from " +
            "`exam` where `status`=#{status} limit #{from},#{num}")
    List<ExamInfo> getExamsByStatus(int status,int from,int num);

    @Select("SELECT `exam_file` FROM `exam` where `exam_id`=#{exam_id}")
    String getExamFile(int exam_id);

    @Select("Select `exam_id`,`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_status` from" +
            " `exam` where `exam_name`=#{name} ")
    ExamInfo getExamIDByExamName(String name);

    @Insert("INSERT INTO `exam`(`exam_name`,`exam_begin`,`exam_len`,`exam_note`,`exam_file`) VALUES (" +
            "#{name},#{beg},#{len},#{note},#{file})")
    int uploadExam(String name, Date beg,String note,String file,Integer len);

    @Delete("DELETE FROM `exam` WHERE `exam_id`=#{exam_id}")
    int deleteExam(int exam_id);

    @Update("UPDATE `exam` SET `exam_status`=#{status} WHERE `exam_id`=#{exam_id}")
    int updateExamStatus(int exam_id,int status);

}
